package fr.simplex_software.payara_hackathon.bank.jms;

import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import fr.simplex_software.payara_hackathon.bank.mapping.*;
import jakarta.annotation.*;
import jakarta.ejb.Singleton;
import jakarta.ejb.*;
import jakarta.inject.*;
import org.apache.activemq.*;
import org.eclipse.microprofile.config.inject.*;
import org.eclipse.microprofile.rest.client.inject.*;
import org.slf4j.*;

import javax.jms.Message;
import javax.jms.*;

@Singleton
@Startup
public class MoneyTransferConsumer
{
  @Inject
  @ConfigProperty(name = "money-transfer.broker.url")
  private String brokerUrl;
  @Inject
  @ConfigProperty(name = "money-transfer.queue.name")
  private String queueName;
  @Inject
  @RestClient
  private MoneyTransferRestClient moneyTransferRestClient;

  private Connection connection;
  private Session session;
  private Destination queue;
  private MessageConsumer messageConsumer;
  private static final Logger slf4jLogger = LoggerFactory.getLogger(MoneyTransferConsumer.class);

  @PostConstruct
  public void postConstruct()
  {
    slf4jLogger.info("*** MoneyTransferConsumer.postConstruct()");
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);
    activeMQConnectionFactory.setTrustAllPackages(true);
    try
    {
      connection = activeMQConnectionFactory.createConnection();
      connection.start();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      queue = session.createQueue(queueName);
      messageConsumer = session.createConsumer(queue);
      messageConsumer.setMessageListener(new MoneyTransferMessageListener());
    }
    catch (JMSException e)
    {
      throw new RuntimeException(e);
    }
  }

  @PreDestroy
  public void preDestroy()
  {
    slf4jLogger.info("*** MoneyTransferConsumer.preDestroy()");
    try
    {
      messageConsumer.close();
      session.close();
      connection.stop();
      connection.close();
    }
    catch (JMSException e)
    {
      throw new RuntimeException(e);
    }
    messageConsumer = null;
    queue = null;
    session = null;
    connection = null;
  }

  public class MoneyTransferMessageListener implements MessageListener
  {
    @Override
    public void onMessage(Message message)
    {
      ObjectMessage objectMessage = (ObjectMessage) message;
      try
      {
        MoneyTransfer moneyTransfer = (MoneyTransfer) objectMessage.getObject();
        slf4jLogger.info("*** MoneyTransferMessageListener.onMessage(): Have received a money transfer order with reference {}", moneyTransfer.getReference());
        moneyTransferRestClient.createMoneyTransferOrder(MoneyTransferMapper.INSTANCE.toEntity(moneyTransfer));
        slf4jLogger.info("*** MoneyTransferMessageListener.onMessage(): Have created the money transfer order with reference {}", moneyTransfer.getReference());
      }
      catch (JMSException e)
      {
        throw new RuntimeException(e);
      }
    }
  }
}

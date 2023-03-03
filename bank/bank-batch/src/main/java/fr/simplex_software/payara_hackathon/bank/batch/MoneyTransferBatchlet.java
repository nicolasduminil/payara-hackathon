package fr.simplex_software.payara_hackathon.bank.batch;

import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import jakarta.batch.api.*;
import jakarta.batch.runtime.*;
import jakarta.ejb.*;
import jakarta.inject.*;
import jakarta.xml.bind.*;
import org.apache.activemq.*;
import org.eclipse.microprofile.config.inject.*;
import org.slf4j.*;

import javax.jms.*;
import java.io.*;

@Named
@Stateless
public class MoneyTransferBatchlet extends AbstractBatchlet implements Serializable
{
  private static final Logger slf4jLogger = LoggerFactory.getLogger(MoneyTransferBatchlet.class);

  @Inject
  @ConfigProperty(name = "money-transfer.source.file.name")
  private String sourceFileName;
  @Inject
  @ConfigProperty(name = "money-transfer.broker.url")
  private String brokerUrl;
  @Inject
  @ConfigProperty(name = "money-transfer.queue.name")
  private String queueName;

  @Override
  public String process() throws Exception
  {
    slf4jLogger.info("*** MoneyTransferBatchlet.process(): Running ...");
    Connection connection = new ActiveMQConnectionFactory(brokerUrl).createConnection();
    connection.start();
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Destination queue = session.createQueue(queueName);
    MessageProducer producer = session.createProducer(queue);
    JAXBContext jaxbContext = JAXBContext.newInstance(MoneyTransfers.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    InputStream is = this.getClass().getClassLoader().getResourceAsStream(sourceFileName);
    MoneyTransfers mts = (MoneyTransfers) jaxbUnmarshaller.unmarshal(is);
    mts.getMoneyTransfers().forEach(mt ->
    {
      try
      {
        producer.send(queue, session.createObjectMessage(mt));
        slf4jLogger.info("*** MoneyTransferBatchlet.process(): Have produced a money transfer order having refernce {}", mt.getReference());
      }
      catch (JMSException e)
      {
        throw new RuntimeException(e);
      }
    });
    if (connection != null)
      connection.close();
    return BatchStatus.COMPLETED.name();
  }
}

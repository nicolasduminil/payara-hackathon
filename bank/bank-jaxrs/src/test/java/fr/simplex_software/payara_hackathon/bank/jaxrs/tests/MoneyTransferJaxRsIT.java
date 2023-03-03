package fr.simplex_software.payara_hackathon.bank.jaxrs.tests;

import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;
import org.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.*;

import java.math.*;
import java.net.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoneyTransferJaxRsIT
{
  private Client client;
  private WebTarget webTarget;
  private static final String APP_URL = "http://%s:%d/bank-jaxrs/money/xfer";
  private static URI uri;

  @Container
  private static final GenericContainer<?> payara =
    new GenericContainer<>("payara/server-full:6.2023.2-jdk17")
      .withExposedPorts(4848, 8080)
      .withFileSystemBind("target/bank-jaxrs.war",
        "/opt/payara/deployments/bank-jaxrs.war", BindMode.READ_ONLY)
      .withLogConsumer(
        new Slf4jLogConsumer(LoggerFactory.getLogger(MoneyTransferJaxRsIT.class)))
      .waitingFor(Wait.forLogMessage(".*JMXStartupService has started.*", 1));

  @BeforeAll
  public static void beforeAll()
  {
    uri = UriBuilder.fromUri(
        String.format(APP_URL, payara.getHost(), payara.getMappedPort(8080)))
      .build();
  }

  @BeforeEach
  public void beforeEach()
  {
    client = ClientBuilder.newClient();
    webTarget = client.target(
      String.format(APP_URL, payara.getHost(), payara.getMappedPort(8080)));
  }

  @AfterAll
  public static void afterAll()
  {
    payara.stop();
    uri = null;
  }

  @AfterEach
  public void afterEach()
  {
    if (client != null)
    {
      client.close();
      client = null;
    }
    webTarget = null;
  }

  @Test
  @Order(60)
  public void testCreateCustomerShouldSucceedWith201()
  {
    Response response = webTarget.request().accept(MediaType.APPLICATION_JSON)
      .post(Entity.entity(
        new MoneyTransferEntity("reference",
          new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
            new BankEntity("bankName", List.of(new BankAddressEntity("streetName", "10",
              "cityName", "poBox", "zipCode", "countryName")))),
          new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
            new BankEntity("bankName", List.of(new BankAddressEntity("streetName", "10",
              "cityName", "poBox", "zipCode", "countryName")))),
          new BigDecimal(100.00)),
        MediaType.APPLICATION_JSON));
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_CREATED);
  }

  @Test
  @Order(70)
  public void testGetMoneyTransferOrdersShouldSucceedWith200()
  {
    Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).get();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    List<MoneyTransfer> moneyTransferList = response.readEntity(new GenericType<List<MoneyTransfer>>(){});
    assertThat(moneyTransferList).isNotNull();
    assertThat(moneyTransferList.size()).isEqualTo(1);
    MoneyTransfer moneyTransfer = moneyTransferList.get(0);
    BankAccount bankAccount = moneyTransfer.getSourceAccount();
    assertBankAccount(bankAccount);
  }

  @Test
  @Order(80)
  public void testUpdateMoneyTransferOrderShouldSucceedWith202()
  {
    Response response = webTarget.path("{ref}").resolveTemplate("ref", "reference").request()
      .accept(MediaType.APPLICATION_JSON)
      .put(Entity.entity(new MoneyTransferEntity("*** Updated",
        new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
          new BankEntity("bankName", List.of(new BankAddressEntity("streetName", "10",
            "cityName", "poBox", "zipCode", "countryName")))),
        new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
          new BankEntity("bankName", List.of(new BankAddressEntity("streetName", "10",
            "cityName", "poBox", "zipCode", "countryName")))),
        new BigDecimal(100.00)), MediaType.APPLICATION_JSON));
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_ACCEPTED);
  }

  @Test
  @Order(90)
  public void testGetMoneyTransferOrderShouldSucceedWith200()
  {
    Response response = webTarget.path("{ref}").resolveTemplate("ref", "*** Updated")
      .request().get();
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    Optional<MoneyTransfer> moneyTransferOptional = response.readEntity(new GenericType<Optional<MoneyTransfer>>(){});
    assertThat(moneyTransferOptional).isNotNull();
    assertBankAccount(moneyTransferOptional.get().getSourceAccount());
  }

  @Test
  @Order(100)
  public void testDeleteMoneyTransferOrderShouldSucceedWith200()
  {
    Response response = webTarget.path("{ref}").resolveTemplate("ref", "*** Updated")
      .request().get();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    Optional<MoneyTransfer> moneyTransferOptional = response.readEntity(new GenericType<Optional<MoneyTransfer>>(){});
    MoneyTransfer moneyTransfer = moneyTransferOptional.orElseThrow();
    response = webTarget.path("{ref}").resolveTemplate("ref", "*** Updated").request().delete();
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
  }

  private void assertMoneyTransfer(MoneyTransfer moneyTransfer)
  {
    assertThat(moneyTransfer).isNotNull();
    assertThat(moneyTransfer.getReference()).isEqualTo("reference");
    assertThat(moneyTransfer.getAmount()).isEqualTo(100.00);
    assertBankAccount(moneyTransfer.getSourceAccount());
    assertBankAccount(moneyTransfer.getTargetAccount());
  }

  private void assertBankAccount(BankAccount bankAccount)
  {
    assertThat(bankAccount).isNotNull();
    assertThat(bankAccount.getAccountID()).isEqualTo("accountId");
    Bank bank = bankAccount.getBank();
    assertThat(bank).isNotNull();
    assertThat(bank.getBankName()).isEqualTo("bankName");
  }

  private void assertBankAddressList(List<BankAddress> bankAddressList)
  {
    assertThat(bankAddressList).isNotNull();
    assertThat(bankAddressList.size()).isEqualTo(1);
    BankAddress bankAddress = bankAddressList.get(0);
    assertThat(bankAddress).isNotNull();
    assertThat(bankAddress.getCityName()).isEqualTo("cityName");
    assertThat(bankAddress.getCountryName()).isEqualTo("countryName");
  }
}

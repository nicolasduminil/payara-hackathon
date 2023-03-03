package fr.simplex_software.payara_hackathon.bank.jaxrs.tests;

import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import io.restassured.http.*;
import io.restassured.response.Response;
import jakarta.ws.rs.core.*;
import org.junit.jupiter.api.*;
import org.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.*;

import java.math.*;
import java.net.*;

import static io.restassured.RestAssured.*;
import static java.lang.Thread.*;
import static org.assertj.core.api.Assertions.*;

@Disabled("Disabled as REST-assured doesn't support yet Jakarta EE 10 (JSON-B 3.0)")
@Testcontainers
public class MoneyTransferRestAssuredIT
{
  private static final Logger slf4jLogger = LoggerFactory.getLogger(MoneyTransferRestAssuredIT.class);
  private static final String APP_URL = "http://%s:%d/money-transfer-jaxrs/money/xfer";
  private static URI uri;

  @Container
  private static GenericContainer payara =
    new GenericContainer("payara/server-full:6.2023.2-jdk17")
      .withExposedPorts(4848, 8080)
      .withFileSystemBind("target/money-transfer-jaxrs.war",
        "/opt/payara/deployments/money-transfer-jaxrs.war", BindMode.READ_ONLY)
      .withLogConsumer(new Slf4jLogConsumer(slf4jLogger))
      .waitingFor(Wait.forLogMessage(".*JMXStartupService has started.*", 1));

  @BeforeAll
  public static void beforeAll() throws MalformedURLException
  {
    uri = UriBuilder.fromUri("http://" + payara.getHost())
      .port(payara.getMappedPort(8080))
      .path("test-jdbc").path("secured")
      .build();
  }

  @AfterAll
  public static void after()
  {
    payara.stop();
    uri = null;
  }

  @Test
  @Order(5)
  public void testPayaraShouldRun()
  {
    assertThat(payara).isNotNull();
    assertThat(payara.isRunning()).isTrue();
  }

  @Test
  @Order(10)
  public void testCreateCustomerShouldSucceed() throws MalformedURLException, InterruptedException
  {
    sleep(5000);
    MoneyTransferEntity moneyTransferEntity = new MoneyTransferEntity("reference",
      new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode"),
      new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode"),
      new BigDecimal(100.00));
    Response response = given()
      .contentType(ContentType.JSON)
      .body(moneyTransferEntity)
      .when()
      .post(uri)
      .then()
      .assertThat().statusCode(201)
      .extract().response();
  }
}

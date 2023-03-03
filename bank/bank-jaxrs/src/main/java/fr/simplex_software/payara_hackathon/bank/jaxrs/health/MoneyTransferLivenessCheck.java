package fr.simplex_software.payara_hackathon.bank.jaxrs.health;

import jakarta.enterprise.context.*;
import jakarta.inject.*;
import org.eclipse.microprofile.config.inject.*;
import org.eclipse.microprofile.health.*;

@ApplicationScoped
@Liveness
public class MoneyTransferLivenessCheck implements HealthCheck
{
  @Inject
  @ConfigProperty(name = "freememory.threshold", defaultValue = "10485760")
  private long threshold;

  @Override
  public HealthCheckResponse call()
  {
    HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("System Uptime Health Check");
    long freeMemory = Runtime.getRuntime().freeMemory();
    if (freeMemory < threshold)
      responseBuilder.down()
        .withData("error", "Not enough free memory! Available " + freeMemory +
          ". Please restart application");
    else
      responseBuilder.up()
        .withData("success", "The service is up and running");
    return responseBuilder.build();
  }
}

package fr.simplex_software.payara_hackathon.bank.jaxrs.health;

import jakarta.enterprise.context.*;
import org.eclipse.microprofile.health.*;

@ApplicationScoped
@Readiness
public class MoneyTransferReadinessCheck implements HealthCheck
{
  private String BASE_URL = "http://localhost:38080/money/xfer";

  @Override
  public HealthCheckResponse call()
  {
    return HealthCheckResponse.named("Readiness").withData("services", "available").up().build();
  }
}

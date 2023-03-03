package fr.simplex_software.payara_hackathon.bank.facade.cdi;

import jakarta.enterprise.context.*;
import jakarta.enterprise.inject.*;
import jakarta.persistence.*;

@ApplicationScoped
public class EntityManagerFactoryProducer
{
  @Produces
  public EntityManagerFactory create()
  {
    return Persistence.createEntityManagerFactory("pu-money-xfer");
  }

  public void destroy(@Disposes EntityManagerFactory factory)
  {
    factory.close();
  }
}

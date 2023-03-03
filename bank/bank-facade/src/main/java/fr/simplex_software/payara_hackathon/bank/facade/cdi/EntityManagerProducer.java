package fr.simplex_software.payara_hackathon.bank.facade.cdi;

import jakarta.enterprise.context.*;
import jakarta.enterprise.inject.*;
import jakarta.inject.*;
import jakarta.persistence.*;

@ApplicationScoped
public class EntityManagerProducer
{
  @Inject
  private transient EntityManagerFactory entityManagerFactory;

  @Produces
  @Default
  @RequestScoped
  public EntityManager create()
  {
    return this.entityManagerFactory.createEntityManager();
  }

  public void dispose(@Disposes @Default EntityManager entityManager)
  {
    if (entityManager.isOpen())
      entityManager.close();
  }
}

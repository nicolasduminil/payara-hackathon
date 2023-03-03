package fr.simplex_software.payara_hackathon.bank.facade.cdi;

import fr.simplex_software.payara_hackathon.bank.facade.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import fr.simplex_software.payara_hackathon.bank.mapping.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.persistence.*;
import jakarta.ws.rs.*;

import java.util.*;

@ApplicationScoped
public class MoneyTransferService implements MoneyTransferFacade
{
  @Inject
  private EntityManager entityManager;

  @Override
  public List<MoneyTransferEntity> getMoneyTransferOrders()
  {
    return entityManager.createNamedQuery("getAll").getResultList();
  }

  @Override
  public Optional<MoneyTransferEntity> getMoneyTransferOrder(String reference)
  {
    Query q = entityManager.createNamedQuery("getByReference");
    q.setParameter("reference", reference);
    return Optional.of((MoneyTransferEntity) q.getSingleResult());
  }

  @Override
  public Long createMoneyTransferOrder(MoneyTransferEntity moneyTransferEntity)
  {
    if (moneyTransferEntity.getId() != null)
      throw new WebApplicationException(
        String.format("### MoneyTransferService.createMoneyTransferOrder(): Cannot create entity having ID %s",
        moneyTransferEntity.getId()));
    entityManager.getTransaction().begin();
    entityManager.persist(moneyTransferEntity);
    entityManager.getTransaction().commit();
    return moneyTransferEntity.getId();
  }

  @Override
  public Long updateMoneyTransferOrder(String ref, MoneyTransferEntity moneyTransferEntity)
  {
    Long id = Long.valueOf(0);
    if (moneyTransferEntity.getId() == null)
    {
      Query q = entityManager.createNamedQuery("getByReference");
      q.setParameter("reference", ref);
      try
      {
        MoneyTransferEntity mte = (MoneyTransferEntity) q.getSingleResult();
        mte = MoneyTransferMapper.INSTANCE.copyMoneyTransferEntity(moneyTransferEntity);
        entityManager.getTransaction().begin();
        id = entityManager.merge(mte).getId();
        entityManager.getTransaction().commit();
      }
      catch (NoResultException ex)
      {
        throw new WebApplicationException(
          String.format("### MoneyTransferService.updateMoneyTransferOrder(): Cannot find money transfer order having reference %s", ref));
      }
    }
    else
      throw new WebApplicationException(
        String.format("### MoneyTransferService.updateMoneyTransferOrder(): Cannot create money transfer order having reference %s because it already exists with ID %s",
          ref, moneyTransferEntity.getId()));
    return id;
  }

  @Override
  public void deleteMoneyTransferOrder(String ref)
  {
    Query q = entityManager.createNamedQuery("getByReference");
    q.setParameter("reference", ref);
    try
    {
      MoneyTransferEntity moneyTransferEntity = (MoneyTransferEntity) q.getSingleResult();
      entityManager.getTransaction().begin();
      entityManager.remove(moneyTransferEntity);
      entityManager.getTransaction().commit();
    }
    catch (NoResultException ex)
    {
      throw new WebApplicationException(
        String.format("### MoneyTransferService.deleteMoneyTransferOrder(): Cannot find money transfer order having reference %s", ref));
    }
  }
}

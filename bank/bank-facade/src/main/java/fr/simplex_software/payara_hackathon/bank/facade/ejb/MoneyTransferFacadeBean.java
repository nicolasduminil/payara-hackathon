package fr.simplex_software.payara_hackathon.bank.facade.ejb;

import fr.simplex_software.payara_hackathon.bank.jpa.*;
import fr.simplex_software.payara_hackathon.bank.mapping.*;
import jakarta.ejb.*;
import jakarta.persistence.*;
import jakarta.ws.rs.*;

import java.util.*;

@Stateless
public class MoneyTransferFacadeBean implements MoneyTransferFacadeLocal, MoneyTransferFacadeRemote
{
  @PersistenceContext(unitName = "pu-money-xfer2")
  private EntityManager entityManager;

  @Override
  public List<MoneyTransferEntity> getMoneyTransferOrders()
  {
    return entityManager.createNamedQuery("getAll").getResultList();
  }

  @Override
  public Optional<MoneyTransferEntity> getMoneyTransferOrder(String reference)
  {
    MoneyTransferEntity moneyTransferEntity = null;
    Query q = entityManager.createNamedQuery("getByReference");
    q.setParameter("reference", reference);
    try
    {
      moneyTransferEntity = (MoneyTransferEntity) q.getSingleResult();
    }
    catch (NoResultException ex)
    {
      throw new WebApplicationException(
        String.format("### MoneyTransferFacadeBean.getMoneyTransferOrder(): Cannot find money transfer order having reference %s", reference));
    }
    return Optional.ofNullable(moneyTransferEntity);
  }

  @Override
  public Long createMoneyTransferOrder(MoneyTransferEntity moneyTransferEntity)
  {
    if (moneyTransferEntity.getId() != null)
      throw new WebApplicationException(
        String.format("### MoneyTransferFacadeBean.createMoneyTransferOrder(): Cannot create entity having ID %s",
          moneyTransferEntity.getId()));
    entityManager.persist(moneyTransferEntity);
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
        entityManager.merge(mte);
        id = mte.getId();
      }
      catch (NoResultException ex)
      {
        throw new WebApplicationException(
          String.format("### MoneyTransferFacadeBean.updateMoneyTransferOrder(): Cannot find money transfer order having reference %s", ref));
      }
    } else
      throw new WebApplicationException(
        String.format("### MoneyTransferFacadeBean.updateMoneyTransferOrder(): Cannot create money transfer order having reference %s because it already exists with ID %s",
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
      entityManager.remove(moneyTransferEntity);
    }
    catch (NoResultException ex)
    {
      throw new WebApplicationException(
        String.format("### MoneyTransferFacadeBean.deleteMoneyTransferOrder(): Cannot find money transfer order having reference %s", ref));
    }
  }
}

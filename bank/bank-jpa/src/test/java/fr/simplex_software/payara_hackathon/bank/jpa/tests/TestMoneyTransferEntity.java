package fr.simplex_software.payara_hackathon.bank.jpa.tests;

import fr.simplex_software.payara.hackathon.money_transfer.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestMoneyTransferEntity
{
  private static EntityManagerFactory emf;
  private static EntityManager em;
  private static TypedQuery<MoneyTransferEntity> query;
  private static EntityTransaction tx;

  @BeforeAll
  public static void init() throws FileNotFoundException, SQLException
  {
    emf = Persistence.createEntityManagerFactory("pu-money-xfer");
    em = emf.createEntityManager();
    tx = em.getTransaction();
    query =
      em.createQuery("select c from MoneyTransferEntity c", MoneyTransferEntity.class);
  }

  @AfterAll
  public static void tearDown()
  {
    em.clear();
    em.close();
    emf.close();
    tx = null;
    em = null;
    emf = null;
  }

  @Test
  @Order(10)
  public void testCreateMoneyTransferOrder()
  {
    MoneyTransferEntity moneyTransferEntity = new MoneyTransferEntity("reference",
      new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
        new BankEntity("bankName", Arrays.asList(new BankAddressEntity("streetName", "10",
          "cityName", "poBox", "zipCode", "countryName")))),
      new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
        new BankEntity("bankName", Arrays.asList(new BankAddressEntity("streetName", "10",
          "cityName", "poBox", "zipCode", "countryName")))),
      new BigDecimal(100.00));
    tx.begin();
    em.persist(moneyTransferEntity);
    tx.commit();
    moneyTransferEntity = query.getSingleResult();
    assertThat(moneyTransferEntity).isNotNull();
    assertThat(moneyTransferEntity.getId()).isNotNull();
  }

  @Test
  @Order(20)
  public void testGetMoneyTransferOrders()
  {
    List<MoneyTransferEntity> moneyTransferEntityList = query.getResultList();
    assertThat(moneyTransferEntityList).isNotNull();
    assertThat(moneyTransferEntityList.size()).isEqualTo(1);
    MoneyTransferEntity moneyTransferEntity = moneyTransferEntityList.get(0);
    BankAccountEntity bankAccountEntity = moneyTransferEntity.getSourceAccount();
    assertBankAccountEntity(bankAccountEntity);
  }

  @Test
  @Order(30)
  public void testUpdateMoneyTransferOrder()
  {
    MoneyTransferEntity moneyTransferEntity = query.getSingleResult();
    moneyTransferEntity.setReference("***Updated");
    tx.begin();
    em.merge(moneyTransferEntity);
    tx.commit();
    moneyTransferEntity = query.getSingleResult();
    assertThat(moneyTransferEntity.getReference()).isEqualTo("***Updated");
  }

  @Test
  @Order(40)
  public void testDeleteMoneyTransferOrder()
  {
    MoneyTransferEntity moneyTransferEntity = query.getSingleResult();
    tx.begin();
    em.remove(moneyTransferEntity);
    tx.commit();
    assertThrows (NoResultException.class, () -> query.getSingleResult(), "NoResultException was expected");
  }

  private void assertMoneyTransferEntity(MoneyTransferEntity moneyTransferEntity)
  {
    assertThat(moneyTransferEntity).isNotNull();
    assertThat(moneyTransferEntity.getId()).isNotNull();
    assertThat(moneyTransferEntity.getReference()).isEqualTo("reference");
    assertThat(moneyTransferEntity.getAmount()).isEqualTo(100.00);
    assertBankAccountEntity(moneyTransferEntity.getSourceAccount());
    assertBankAccountEntity(moneyTransferEntity.getTargetAccount());
  }

  private void assertBankAccountEntity(BankAccountEntity bankAccountEntity)
  {
    assertThat(bankAccountEntity).isNotNull();
    assertThat(bankAccountEntity.getId()).isNotNull();
    assertThat(bankAccountEntity.getAccountID()).isEqualTo("accountId");
    BankEntity bankEntity = bankAccountEntity.getBank();
    assertThat(bankEntity).isNotNull();
    assertThat(bankEntity.getId()).isNotNull();
    assertThat(bankEntity.getBankName()).isEqualTo("bankName");
    assertBankAddressEntityList(bankEntity.getBankAddresses());
  }

  private void assertBankAddressEntityList(List<BankAddressEntity> bankAddressEntityList)
  {
    assertThat(bankAddressEntityList).isNotNull();
    assertThat(bankAddressEntityList.size()).isEqualTo(1);
    BankAddressEntity bankAddressEntity = bankAddressEntityList.get(0);
    assertThat(bankAddressEntity).isNotNull();
    assertThat(bankAddressEntity.getCityName()).isEqualTo("cityName");
    assertThat(bankAddressEntity.getCountryName()).isEqualTo("countryName");
  }
}

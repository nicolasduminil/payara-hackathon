package fr.simplex_software.payara_hackathon.bank.facade.tests;

import fr.simplex_software.payara_hackathon.bank.facade.cdi.*;
import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.persistence.*;
import org.jboss.weld.junit5.auto.*;
import org.junit.jupiter.api.*;

import java.math.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages({MoneyTransferService.class, EntityManagerFactoryProducer.class, EntityManagerProducer.class})
@ActivateScopes(RequestScoped.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoneyTransferFacadeIT
{
  @Inject
  private MoneyTransferService moneyTransferService;

  @Test
  @Order(10)
  public void testMoneyTransferServiceShouldBeNotNull()
  {
    assertThat(moneyTransferService).isNotNull();
  }

  @Test
  @Order(20)
  public void testCreateMoneyTransferOrderShouldSucceed()
  {
    MoneyTransferEntity moneyTransferEntity = new MoneyTransferEntity("reference",
      new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
        new BankEntity("bankName", Arrays.asList(new BankAddressEntity("streetName", "10",
          "cityName", "poBox", "zipCode", "countryName")))),
      new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
        new BankEntity("bankName", Arrays.asList(new BankAddressEntity("streetName", "10",
          "cityName", "poBox", "zipCode", "countryName")))),
      new BigDecimal(100.00));
    Long id = moneyTransferService.createMoneyTransferOrder(moneyTransferEntity);
    assertThat(id).isNotNull();
  }

  @Test
  @Order(30)
  public void testGetMoneyTransferOrdersShouldSucceed()
  {
    List<MoneyTransferEntity> moneyTransferEntityList = moneyTransferService.getMoneyTransferOrders();
    assertThat(moneyTransferEntityList).isNotNull();
    assertThat(moneyTransferEntityList.size()).isEqualTo(1);
    MoneyTransferEntity moneyTransferEntity = moneyTransferEntityList.get(0);
    BankAccountEntity bankAccountEntity = moneyTransferEntity.getSourceAccount();
    assertBankAccountEntity(bankAccountEntity);
  }

  @Test
  @Order(40)
  public void testUpdateMoneyTransferOrderShouldSucceed()
  {
    MoneyTransferEntity moneyTransferEntity = new MoneyTransferEntity("*** Updated",
      new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
        new BankEntity("bankName", Arrays.asList(new BankAddressEntity("streetName", "10",
          "cityName", "poBox", "zipCode", "countryName")))),
      new BankAccountEntity("accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode",
        new BankEntity("bankName", Arrays.asList(new BankAddressEntity("streetName", "10",
          "cityName", "poBox", "zipCode", "countryName")))),
      new BigDecimal(100.00));
    moneyTransferService.updateMoneyTransferOrder("reference", moneyTransferEntity);
    moneyTransferEntity = moneyTransferService.getMoneyTransferOrder("*** Updated").orElseThrow();
    assertThat(moneyTransferEntity).isNotNull();
    assertThat(moneyTransferEntity.getReference()).isEqualTo("*** Updated");
  }

  @Test
  @Order(50)
  public void testDeleteMoneyTransferOrderShouldSucceed()
  {
    moneyTransferService.deleteMoneyTransferOrder("*** Updated");
    assertThrows (NoResultException.class, () -> moneyTransferService.getMoneyTransferOrder("*** Updated").orElseThrow(), "NoResultException was expected");
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

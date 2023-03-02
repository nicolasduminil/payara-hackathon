package fr.simplex_software.payara_hackathon.bank.mapping.tests;

import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import fr.simplex_software.payara_hackathon.bank.mapping.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

public class TestBankMapper
{
  @Test
  public void testMapBankToBankEntity()
  {
    Bank bank = new Bank(Arrays.asList(new BankAddress("streetName", "streetNumber",
      "poBox", "cityName", "zipCode", "countryName")), "bankName");
    BankEntity bankEntity = BankMapper.INSTANCE.toEntity(bank);
    assertThat(bankEntity).isNotNull();
    assertThat(bankEntity.getBankName()).isEqualTo("bankName");
    List<BankAddressEntity> addressEntities = bankEntity.getBankAddresses();
    assertThat(addressEntities).isNotNull();
    assertThat(addressEntities.size()).isEqualTo(1);
    BankAddressEntity bankAddressEntity = addressEntities.get(0);
    assertThat(bankAddressEntity).isNotNull();
    assertThat(bankAddressEntity.getCityName()).isEqualTo("cityName");
    assertThat(bankAddressEntity.getCountryName()).isEqualTo("countryName");
  }

  @Test
  public void testMapBankEntityToBank()
  {
    BankEntity bankEntity = new BankEntity( "bankName", Arrays.asList(new BankAddressEntity("streetName", "streetNumber",
      "cityName", "poBox", "zipCode", "countryName")));
    Bank bank = BankMapper.INSTANCE.fromEntity(bankEntity);
    assertThat(bank).isNotNull();
    assertThat(bank.getBankName()).isEqualTo("bankName");
    List<BankAddress> addresses = bank.getBankAddresses();
    assertThat(addresses).isNotNull();
    assertThat(addresses.size()).isEqualTo(1);
    BankAddress bankAddress = addresses.get(0);
    assertThat(bankAddress).isNotNull();
    assertThat(bankAddress.getCityName()).isEqualTo("cityName");
    assertThat(bankAddress.getCountryName()).isEqualTo("countryName");
  }
}

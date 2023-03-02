package fr.simplex_software.payara_hackathon.bank.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "BANKS")
public class BankEntity
{
  @Id
  @GeneratedValue
  @Column(name = "BANK_ID", unique = true, nullable = false, length = 8)
  private Long id;
  @NotNull
  @Column (name = "BANK_NAME", nullable = false, length = 30)
  private String bankName;
  @NotNull
  @ElementCollection
  @CollectionTable (name = "BANK_ADDRESSES")
  @AttributeOverride(name = "streetName", column = @Column (name = "STREET_NAME", nullable = false, length = 80))
  @AttributeOverride(name = "streetNumber", column = @Column (name = "STREET_NUMBER", nullable = false, length = 10))
  @AttributeOverride(name = "cityName", column = @Column (name = "CITY_NAME", nullable = false, length = 40))
  @AttributeOverride(name = "poBox", column = @Column (name = "PO_BOX", nullable = false, length = 10))
  @AttributeOverride(name = "zipCode", column = @Column (name = "ZIP_CODE", nullable = false, length = 10))
  @AttributeOverride(name = "countryName", column = @Column (name = "COUNTRY_NAME", nullable = false, length = 20))
  private List<BankAddressEntity> bankAddresses = new ArrayList<>();

  public BankEntity()
  {
  }

  public BankEntity(String bankName, List<BankAddressEntity> bankAddresses)
  {
    this.bankName = bankName;
    this.bankAddresses = bankAddresses;
  }

  public BankEntity(Long id, String bankName, List<BankAddressEntity> bankAddresses)
  {
    this (bankName, bankAddresses);
    this.id = id;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getBankName()
  {
    return bankName;
  }

  public void setBankName(String bankName)
  {
    this.bankName = bankName;
  }

  public List<BankAddressEntity> getBankAddresses()
  {
    return bankAddresses;
  }

  public void setBankAddresses(List<BankAddressEntity> bankAddresses)
  {
    this.bankAddresses = bankAddresses;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    BankEntity that = (BankEntity) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(getId());
  }
}

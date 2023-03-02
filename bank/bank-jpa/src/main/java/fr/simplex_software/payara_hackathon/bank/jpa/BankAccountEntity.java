package fr.simplex_software.payara_hackathon.bank.jpa;

import fr.simplex_software.payara.hackathon.money_transfer.jaxb.*;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "BANK_ACCOUNTS")
public class BankAccountEntity
{
  @Id
  @GeneratedValue
  @Column(name = "BANK_ACCOUNT_ID", unique = true, nullable = false, length = 8)
  private Long id;
  @Column(name = "ACCOUNT_ID", nullable = false, length = 10)
  private String accountID;
  @Enumerated
  @Column(name = "ACCOUNT_TYPE", nullable = false)
  private BankAccountType accountType;
  @Column(name = "SORT_CODE", nullable = false, length = 20)
  private String sortCode;
  @Column(name = "ACCOUNT_NUMBER", nullable = false, length = 20)
  private String accountNumber;
  @Column(name = "TRANS_CODE", nullable = false, length = 20)
  private String transCode;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "BANK_ID", updatable = false, insertable = false)
  private BankEntity bank;
  public BankAccountEntity()
  {
  }

  public BankAccountEntity(String accountID, BankAccountType accountType, String sortCode, String accountNumber, String transCode)
  {
    this.accountID = accountID;
    this.accountType = accountType;
    this.sortCode = sortCode;
    this.accountNumber = accountNumber;
    this.transCode = transCode;
  }

  public BankAccountEntity(Long id, String accountID, BankAccountType accountType, String sortCode, String accountNumber, String transCode)
  {
    this (accountID, accountType, sortCode, accountNumber, transCode);
    this.id = id;
  }

  public BankAccountEntity(String accountID, BankAccountType accountType, String sortCode, String accountNumber, String transCode, BankEntity bank)
  {
    this (accountID, accountType, sortCode, accountNumber, transCode);
    this.bank = bank;
  }
  public BankAccountEntity(Long id, String accountID, BankAccountType accountType, String sortCode, String accountNumber, String transCode, BankEntity bank)
  {
    this (id, accountID, accountType, sortCode, accountNumber, transCode);
    this.bank = bank;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getAccountID()
  {
    return accountID;
  }

  public void setAccountID(String accountID)
  {
    this.accountID = accountID;
  }

  public BankAccountType getAccountType()
  {
    return accountType;
  }

  public void setAccountType(BankAccountType accountType)
  {
    this.accountType = accountType;
  }

  public String getSortCode()
  {
    return sortCode;
  }

  public void setSortCode(String sortCode)
  {
    this.sortCode = sortCode;
  }

  public String getAccountNumber()
  {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber)
  {
    this.accountNumber = accountNumber;
  }

  public String getTransCode()
  {
    return transCode;
  }

  public void setTransCode(String transCode)
  {
    this.transCode = transCode;
  }

  public BankEntity getBank()
  {
    return bank;
  }

  public void setBank(BankEntity bank)
  {
    this.bank = bank;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    BankAccountEntity that = (BankAccountEntity) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(getId());
  }
}

package fr.simplex_software.payara_hackathon.bank.jpa;

import jakarta.persistence.*;

import java.math.*;
import java.util.*;

@Entity
@Table(name = "MONEY_TRANSFERS")
@NamedQuery(name = "getAll", query = "select mt from MoneyTransferEntity mt order by mt.id")
@NamedQuery(name = "getByReference", query = "select mt from MoneyTransferEntity mt where mt.reference = :reference")
public class MoneyTransferEntity
{
  @Id
  @GeneratedValue
  @Column(name = "MONEY_TRANSFER_ID", unique = true, nullable = false, length = 8)
  private Long id;
  @Column(name = "MONEY_TRASFER_REFERENCE", unique = true, nullable = false, length = 40)
  private String reference;
  @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
  @JoinColumn(unique = true)
  private BankAccountEntity sourceAccount;
  @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
  @JoinColumn(unique = true)
  private BankAccountEntity targetAccount;
  @Column (name = "TRANSFER_AMOUNT", nullable = false, length = 20)
  private BigDecimal amount;

  public MoneyTransferEntity()
  {
  }

  public MoneyTransferEntity(String reference, BankAccountEntity sourceAccount, BankAccountEntity targerAccount, BigDecimal amount)
  {
    this.reference = reference;
    this.sourceAccount = sourceAccount;
    this.targetAccount = targerAccount;
    this.amount = amount;
  }

  public MoneyTransferEntity(Long id, String reference, BankAccountEntity sourceAccount, BankAccountEntity targerAccount, BigDecimal amount)
  {
    this (reference, sourceAccount, targerAccount, amount);
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

  public String getReference()
  {
    return reference;
  }

  public void setReference(String reference)
  {
    this.reference = reference;
  }

  public BankAccountEntity getSourceAccount()
  {
    return sourceAccount;
  }

  public void setSourceAccount(BankAccountEntity sourceAccount)
  {
    this.sourceAccount = sourceAccount;
  }

  public BankAccountEntity getTargetAccount()
  {
    return targetAccount;
  }

  public void setTargetAccount(BankAccountEntity targerAccount)
  {
    this.targetAccount = targerAccount;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MoneyTransferEntity that = (MoneyTransferEntity) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(getId());
  }
}

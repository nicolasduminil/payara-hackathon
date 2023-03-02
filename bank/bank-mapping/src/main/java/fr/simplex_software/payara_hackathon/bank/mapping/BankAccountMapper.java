package fr.simplex_software.payara_hackathon.bank.mapping;

import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper
public interface BankAccountMapper
{
  BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);
  BankAccount fromEntity (BankAccountEntity bankAccountEntity);
  BankAccountEntity toEntity (BankAccount bankAccount);
  BankAccountEntity copyBankAccountEntity (BankAccountEntity bankAccountEntity);
  BankAccount copyBankAccount (BankAccount bankAccount);
}

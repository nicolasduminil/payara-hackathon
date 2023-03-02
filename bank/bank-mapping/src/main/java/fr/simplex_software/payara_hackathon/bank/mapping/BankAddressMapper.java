package fr.simplex_software.payara_hackathon.bank.mapping;

import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper
public interface BankAddressMapper
{
  BankAddressMapper INSTANCE = Mappers.getMapper(BankAddressMapper.class);
  BankAddressEntity toEntity (BankAddress bankAddress);
  BankAddress fromEntity (BankAddressEntity bankAddressEntity);
  BankAddressEntity copyBankAddressEntity (BankAddressEntity bankAddressEntity);
  BankAddress copyBankAddress (BankAddress bankAddress);
}

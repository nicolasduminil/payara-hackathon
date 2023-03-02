package fr.simplex_software.payara_hackathon.bank.mapping;

import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper
public interface BankMapper
{
  BankMapper INSTANCE = Mappers.getMapper(BankMapper.class);
  Bank fromEntity (BankEntity bankEntity);
  BankEntity toEntity (Bank bank);
  BankEntity copyBankEntity (BankEntity bankEntity);
  Bank copyBank (Bank bank);
}

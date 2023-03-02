package fr.simplex_software.payara_hackathon.bank.mapping;

import fr.simplex_software.payara_hackathon.bank.jpa.*;
import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper
public interface MoneyTransferMapper
{
  MoneyTransferMapper INSTANCE = Mappers.getMapper(MoneyTransferMapper.class);
  MoneyTransfer fromEntity (MoneyTransferEntity moneyTransferEntity);
  MoneyTransferEntity toEntity (MoneyTransfer moneyTransfer);
  @Mapping(target="id", ignore = true)
  MoneyTransferEntity copyMoneyTransferEntity (MoneyTransferEntity moneyTransferEntity);
  MoneyTransfer copyMoneyTransfer (MoneyTransfer moneyTransfer);
}

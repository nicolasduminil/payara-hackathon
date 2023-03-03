package fr.simplex_software.payara_hackathon.bank.facade;

import fr.simplex_software.payara_hackathon.bank.jpa.*;

import java.io.*;
import java.util.*;

public interface MoneyTransferFacade extends Serializable
{
  List<MoneyTransferEntity> getMoneyTransferOrders();
  Optional<MoneyTransferEntity> getMoneyTransferOrder(String reference);
  Long createMoneyTransferOrder(MoneyTransferEntity moneyTransfer);
  Long updateMoneyTransferOrder(String ref, MoneyTransferEntity moneyTransfer);
  void deleteMoneyTransferOrder(String ref);
}

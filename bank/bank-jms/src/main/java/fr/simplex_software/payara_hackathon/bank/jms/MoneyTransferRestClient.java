package fr.simplex_software.payara_hackathon.bank.jms;

import fr.simplex_software.payara_hackathon.bank.jpa.*;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.*;

import java.util.*;

import static jakarta.ws.rs.core.MediaType.*;

@RegisterRestClient(baseUri = "http://192.168.96.13:8080/bank-jaxrs/money/xfer")
public interface MoneyTransferRestClient
{
  @GET
  @Produces(APPLICATION_JSON)
  List<MoneyTransferEntity> getMoneyTransferOrders();
  @GET
  @Path("{ref}")
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  Optional<MoneyTransferEntity> getMoneyTransferOrder(@PathParam("ref") String reference);
  @POST
  @Consumes(APPLICATION_JSON)
  Long createMoneyTransferOrder(MoneyTransferEntity moneyTransfer);
  @PUT
  @Path("{ref}")
  @Consumes(APPLICATION_JSON)
  Long updateMoneyTransferOrder(@PathParam("ref") String ref, MoneyTransferEntity moneyTransfer);
  @DELETE
  @Path("{ref}")
  @Consumes(APPLICATION_JSON)
  void deleteMoneyTransferOrder(@PathParam("ref") String ref);
}

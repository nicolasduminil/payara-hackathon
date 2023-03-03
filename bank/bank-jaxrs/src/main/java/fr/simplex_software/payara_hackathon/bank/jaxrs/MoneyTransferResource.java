package fr.simplex_software.payara_hackathon.bank.jaxrs;

import fr.simplex_software.payara_hackathon.bank.facade.ejb.*;
import fr.simplex_software.payara_hackathon.bank.jaxb.*;
import fr.simplex_software.payara_hackathon.bank.jpa.*;
import fr.simplex_software.payara_hackathon.bank.mapping.*;
import jakarta.ejb.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.*;
import org.eclipse.microprofile.metrics.*;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.media.*;
import org.eclipse.microprofile.openapi.annotations.responses.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static jakarta.ws.rs.core.MediaType.*;

@Path("xfer")
public class MoneyTransferResource
{
  private static final Logger slf4jLogger = LoggerFactory.getLogger(MoneyTransferResource.class);

  @EJB
  private MoneyTransferFacadeLocal moneyTransferFacadeLocal;

  @GET
  @Path("live")
  @Produces(TEXT_PLAIN)
  public Response testIfLive()
  {
    return Response.ok("Live").build();
  }

  @GET
  @Path("redy")
  @Produces(TEXT_PLAIN)
  public Response testIfReady()
  {
    return Response.ok("Ready").build();
  }

  @GET
  @Produces(APPLICATION_JSON)
  @Operation(description = "Get the money transfer orders list")
  @APIResponse(responseCode = "404", description = "No money transfer orders found",
    content = @Content(mediaType = APPLICATION_JSON))
  @APIResponseSchema(value = MoneyTransferEntity.class, responseDescription = "Money transfer orders list", responseCode = "200")
  @Metered (name = "Get money transfer orders", unit = MetricUnits.MINUTES, description = "Metric to monitor the frequency of the getMoneyTransferOrders endpoint invocations", absolute = true)
  @Timeout(250)
  public Response getMoneyTransferOrders()
  {
    GenericEntity<List<MoneyTransferEntity>> orders = new GenericEntity<>(moneyTransferFacadeLocal.getMoneyTransferOrders()){};
    return Response.ok().entity(orders.getEntity().stream().map(mt -> MoneyTransferMapper.INSTANCE.fromEntity(mt)).collect(Collectors.toList())).build();
  }

  @GET
  @Path("{ref}")
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  @Operation(description = "Get the money transfer order identified by reference")
  @APIResponse(responseCode = "404", description = "No such a money transfer order found",
    content = @Content(mediaType = APPLICATION_JSON))
  @APIResponseSchema(value = MoneyTransferEntity.class, responseDescription = "Money transfer order found", responseCode = "200")
  @Metered (name = "Get money transfer order", unit = MetricUnits.MINUTES, description = "Metric to monitor the frequency of the getMoneyTransferOrder endpoint invocations", absolute = true)
  @Fallback(fallbackMethod = "fallbackOfGetMoneyTransferOrder")
  public Response getMoneyTransferOrder(@PathParam("ref") String reference)
  {
    return Response.ok().entity(MoneyTransferMapper.INSTANCE.fromEntity(moneyTransferFacadeLocal.getMoneyTransferOrder(reference).orElseThrow())).build();
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @Operation(description = "Create a new money transfer order")
  @APIResponse(responseCode = "500", description = "An internal server error has occurred",
    content = @Content(mediaType = APPLICATION_JSON))
  @APIResponseSchema(value = MoneyTransferEntity.class, responseDescription = "The new money transfer order has been created", responseCode = "201")
  @Metered (name = "Create money transfer order", unit = MetricUnits.MINUTES, description = "Metric to monitor the frequency of the createMoneyTransferOrder endpoint invocations", absolute = true)
  @Timeout(250)
  @Retry (retryOn = TimeoutException.class, maxRetries = 2)
  public Response createMoneyTransferOrder(MoneyTransfer moneyTransfer, @Context UriInfo uriInfo)
  {
    Long id = moneyTransferFacadeLocal.createMoneyTransferOrder(MoneyTransferMapper.INSTANCE.toEntity(moneyTransfer));
    //
    // Doesn't work
    // For some reason raises: SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder"
    // Was unable to fix so replaced by System.out.println
    //
    //slf4jLogger.info ("*** MoneyTransferResource.createMoneyTransferOrder(): Have created money tansfer order with reference {}", moneyTransfer.getReference());
    System.out.println ("*** MoneyTransferResource.createMoneyTransferOrder(): Have created money tansfer order with reference " + moneyTransfer.getReference());
    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
    uriBuilder.path(Long.toString(id));
    return Response.created(uriBuilder.build()).build();
  }

  @PUT
  @Path("{ref}")
  @Consumes(APPLICATION_JSON)
  @Operation(description = "Update a money transfer order")
  @APIResponse(responseCode = "404", description = "The money transfer order does not exist",
    content = @Content(mediaType = APPLICATION_JSON))
  @APIResponseSchema(value = MoneyTransferEntity.class, responseDescription = "The Money transfer orderhas been updated", responseCode = "200")
  @Metered (name = "Update a money transfer order", unit = MetricUnits.MINUTES, description = "Metric to monitor the frequency of the updateMoneyTransferOrder endpoint invocations", absolute = true)
  @CircuitBreaker (successThreshold = 5, requestVolumeThreshold = 4, failureRatio = 0.75, delay = 1000)
  public Response updateMoneyTransferOrder(@PathParam("ref") String ref, MoneyTransfer moneyTransfer)
  {
    Long id = moneyTransferFacadeLocal.updateMoneyTransferOrder(ref, MoneyTransferMapper.INSTANCE.toEntity(moneyTransfer));
    return Response.accepted().build();
  }

  @DELETE
  @Path("{ref}")
  @Consumes(APPLICATION_JSON)
  @Operation(description = "Delete a money transfer order")
  @APIResponse(responseCode = "404", description = "The money transfer order does not exist",
    content = @Content(mediaType = APPLICATION_JSON))
  @APIResponseSchema(value = MoneyTransferEntity.class, responseDescription = "The money transfer order has been deleted", responseCode = "200")
  @Metered (name = "Delete a money transfer order", unit = MetricUnits.MINUTES, description = "Metric to monitor the frequency of the deleteMoneyTransferOrders endpoint invocations", absolute = true)
  public Response deleteMoneyTransferOrder(@PathParam("ref") String reference)
  {
    moneyTransferFacadeLocal.deleteMoneyTransferOrder(reference);
    return Response.ok().build();
  }

  public Response fallbackOfGetMoneyTransferOrder(String reference)
  {
    return Response.noContent().build();
  }
}

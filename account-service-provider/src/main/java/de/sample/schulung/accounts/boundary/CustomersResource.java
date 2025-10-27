package de.sample.schulung.accounts.boundary;

import de.sample.schulung.accounts.boundary.CustomerDto.CustomerStateDto;
import de.sample.schulung.accounts.domain.CustomersService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomersResource {

  @Inject
  CustomersService service;

  @Inject
  CustomerDtoMapper mapper;

  @GET
  public Collection<CustomerDto> getCustomers(
    @QueryParam("state") CustomerStateDto stateFilter
  ) {
    return (
      stateFilter == null
        ? service.getCustomers()
        : service.getCustomersByState(mapper.mapState(stateFilter))
    )
      .map(mapper::map)
      .toList();
  }

  @POST
  public Response createCustomer(@Valid CustomerDto body, @Context UriInfo uriInfo) {
    var customer = mapper.map(body);
    service.createCustomer(customer);

    URI uri = uriInfo
      .getAbsolutePathBuilder()
      .path(customer.getUuid().toString())
      .build();

    return Response
      .created(uri)
      .entity(mapper.map(customer))
      .build();
  }

  @GET
  @Path("/{uuid}")
  public CustomerDto findCustomerById(@PathParam("uuid") UUID uuid) {
    return service
      .findCustomerById(uuid)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @PUT
  @Path("/{uuid}")
  public Response replaceCustomer(@PathParam("uuid") UUID uuid, @Valid CustomerDto customer) {
    customer.setUuid(uuid);
    service.replaceCustomer(mapper.map(customer));
    return Response.noContent().build();
  }

  @DELETE
  @Path("/{uuid}")
  public Response deleteCustomer(@PathParam("uuid") UUID uuid) {
    service.deleteCustomer(uuid);
    return Response.noContent().build();
  }

}

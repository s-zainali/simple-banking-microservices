package org.acme.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import org.acme.entity.TransferRequest;

@RegisterRestClient(configKey = "core-service-api")
@Path("/users")
public interface CoreServiceClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    String listUsers();

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUserDetails(@PathParam("accountNumber") String accountNumber);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response addUser(String userPayloadJSON);

    @DELETE
    @Path("/{accountNumber}")
    Response deleteUser(@PathParam("accountNumber") String accountNumber);

    @PATCH
    @Path("/update-balance")
    Response updateBalance(
        @QueryParam("accountNumber") String accountNumber,
        @QueryParam("action") String action,
        @QueryParam("amount") BigDecimal amount
    );

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    Response transferBalance(TransferRequest transferRequest);
}

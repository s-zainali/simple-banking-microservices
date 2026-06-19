package org.acme.gateway.client;

import org.acme.gateway.dto.TransferRequest;
import org.acme.gateway.dto.UserRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(configKey = "auth-service-api")
@Path("/auth/users")
public interface UserCoreClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject listUsers(@HeaderParam("Authorization") String authHeader);

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    String getUserDetails(@PathParam("accountNumber") String accountNumber, @HeaderParam("Authorization") String authHeader);

    @POST
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    Response addUser(UserRequest userRequest, @HeaderParam("Authorization") String authHeader);


    @DELETE
    @Path("/{accountNumber}")
    Response deleteUser(@PathParam("accountNumber") String accountNumber, @HeaderParam("Authorization") String authHeader);

    @PATCH
    @Path("/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    String updateBalance(
        org.acme.gateway.dto.BalanceUpdateRequest balanceRequest,
        @HeaderParam("Authorization") String authHeader
    );

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response transferBalance(TransferRequest transferRequest, @HeaderParam("Authorization") String authHeader);
}
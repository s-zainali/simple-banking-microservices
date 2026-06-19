package org.acme.gateway.resource;

import org.acme.gateway.client.UserCoreClient;
import org.acme.gateway.dto.BalanceUpdateRequest;
import org.acme.gateway.dto.TransferRequest;
import org.acme.gateway.dto.UserRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/gateway/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserGatewayResource {

    @Inject
    @RestClient
    UserCoreClient userCoreClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@HeaderParam("Authorization") String authHeader) {
        try {
            JsonObject res = userCoreClient.listUsers(authHeader);
            return Response.ok(res).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Access Denied / Core Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails(
            @PathParam("accountNumber") @Pattern(regexp = "^[0-9]+$", message = "Account number must be numeric") String accountNumber,
            @HeaderParam("Authorization") String authHeader) {
        try {
            String res = userCoreClient.getUserDetails(accountNumber, authHeader);
            return Response.ok(res).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Access Denied / Core Error").build();
        }
    }

    @POST
    public Response processAndAddUser(@Valid UserRequest request,
            @HeaderParam("Authorization") String authHeader) {
        try {
            Response downstreamResponse = userCoreClient.addUser(request, authHeader);

            Object entityBody = downstreamResponse.readEntity(String.class);

            return Response.status(downstreamResponse.getStatus())
                    .entity(entityBody)
                    .build();

        } catch (Exception e) {
            System.out.println("Exception Occured: " + e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Gateway Exception Occured: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @DELETE
    @Path("/{accountNumber}")
    public Response processAndDeleteUser(
            @PathParam("accountNumber") @Pattern(regexp = "^[0-9]+$", message = "Account number must be numeric") String accountNumber,
            @HeaderParam("Authorization") String authHeader) {
        try {
            Response downstreamResponse = userCoreClient.deleteUser(accountNumber, authHeader);

            Object entityBody = downstreamResponse.readEntity(String.class);
            return Response.status(downstreamResponse.getStatus())
                    .entity(entityBody)
                    .build();
        } catch (Exception e) {
            System.out.println("Delete error debug trace: " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Access Denied / Core Error: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/balance")
    public Response processAndUpdateBalance(@Valid BalanceUpdateRequest request,
            @HeaderParam("Authorization") String authHeader) {
        try {
            String res = userCoreClient.updateBalance(request, authHeader);
            return Response.ok(res).build();
        } catch (Exception e) {

            e.printStackTrace(); 
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("Access Denied / Core Error: " + e.getMessage())
                           .build();
        }
    }

    @POST
    @Path("/transfer")
    public Response processTransferBalance(@Valid TransferRequest request, @HeaderParam("Authorization") String authHeader) {
        try {
            Response downstreamResponse = userCoreClient.transferBalance(request, authHeader);
            Object entityBody = downstreamResponse.readEntity(String.class);
            return Response.status(downstreamResponse.getStatus()).entity(entityBody).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Access Denied / Core Error: " + e.getMessage()).build();
        }
    }
}
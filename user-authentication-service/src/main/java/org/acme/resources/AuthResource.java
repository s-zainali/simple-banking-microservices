package org.acme.resources;

import org.acme.client.CoreServiceClient;
import org.acme.entity.TransferRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Role matrix (roles now live on user_entities.role):
 *   ADMIN    -> everything
 *   TELLER   -> list, view any user, balance adjustments, transfers
 *   CUSTOMER -> view OWN account, transfer FROM own account only
 */
@Path("/auth/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    @RestClient
    CoreServiceClient coreServiceClient;

    @Inject
    SecurityIdentity identity;

    private boolean isCustomer() {
        return identity.hasRole("CUSTOMER");
    }

    private String currentAccountNumber() {
        return identity.getPrincipal().getName();
    }

    /**
     * Faithfully relay a downstream core-service response (status + body) back
     * to the caller instead of collapsing every outcome to 200/201. This keeps
     * the auth tier a transparent pass-through, mirroring how the gateway tier
     * already propagates this service's status codes.
     */
    private Response relay(Response downstream) {
        String body = downstream.hasEntity() ? downstream.readEntity(String.class) : "";
        return Response.status(downstream.getStatus()).entity(body).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "TELLER"})
    public String authorizedListUsers() {
        return coreServiceClient.listUsers();
    }

    @GET
    @Path("/{accountNumber}")
    @RolesAllowed({"ADMIN", "TELLER", "CUSTOMER"})
    public Response authorizedUserDetails(@PathParam("accountNumber") String accountNumber) {
        // A customer may only inspect their own account.
        if (isCustomer() && !currentAccountNumber().equals(accountNumber)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Customers may only view their own account.")
                    .build();
        }
        return relay(coreServiceClient.getUserDetails(accountNumber));
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response authorizedAddUser(String payload) {
        return relay(coreServiceClient.addUser(payload));
    }

    @DELETE
    @Path("/{accountNumber}")
    @RolesAllowed("ADMIN")
    public Response authorizedDeleteUser(@PathParam("accountNumber") String accountNumber) {
        return relay(coreServiceClient.deleteUser(accountNumber));
    }

    @PATCH
    @Path("/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "TELLER"})
    public Response authorizedUpdateBalance(String rawJsonPayload) {
        jakarta.json.JsonObject json =
                jakarta.json.Json.createReader(new java.io.StringReader(rawJsonPayload)).readObject();
        String accountNumber = json.getString("accountNumber");
        String action = json.getString("action");
        java.math.BigDecimal amount = json.getJsonNumber("amount").bigDecimalValue();

        return relay(coreServiceClient.updateBalance(accountNumber, action, amount));
    }

    @POST
    @Path("/transfer")
    @RolesAllowed({"ADMIN", "TELLER", "CUSTOMER"})
    public Response authorizedTransferBalance(TransferRequest transferRequest) {
        // A customer may only move money OUT of their own account.
        if (isCustomer() && !currentAccountNumber().equals(transferRequest.sourceAccountNumber())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Customers may only transfer from their own account.")
                    .build();
        }
        return relay(coreServiceClient.transferBalance(transferRequest));
    }
}

package org.acme;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import org.acme.UserEntity;

import io.vertx.core.json.JsonObject;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserCoreResource {

    @GET
    public JsonObject listUsers() {
        List<UserEntity> usersList = UserEntity.listAll();
        JsonObject returnObject = new JsonObject();
        returnObject.put("Name", "AccountNumber");
        for (UserEntity user: usersList) {
            returnObject.put(user.getName(), user.getAccountNumber());
        }
        return returnObject;
    }

    @GET
    @Path("/{accountNumber}")
    public Response getUserDetails(@PathParam("accountNumber") String accountNumber) {
        UserEntity user = UserEntity.findById(accountNumber);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        return Response.ok(user).build();
    }

    @POST
    @Transactional
    public Response addUser(UserEntity newUser) {
        if (UserEntity.findById(newUser.getAccountNumber()) != null) {
            return Response.status(Response.Status.CONFLICT).entity("Account already exists").build();
        }
        if (newUser.getRole() == null || newUser.getRole().isBlank()) {
            newUser.setRole("CUSTOMER");
        }
        newUser.persist();
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }

    @DELETE
    @Path("/{accountNumber}")
    @Transactional
    public Response deleteUser(@PathParam("accountNumber") String accountNumber) {
        boolean deleted = UserEntity.deleteById(accountNumber);
        if (!deleted)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok("User deleted successfully").build();
    }

    @PATCH
    @Path("/update-balance")
    @Transactional
    public Response updateBalance(
            @QueryParam("accountNumber") String accountNumber,
            @QueryParam("action") String action,
            @QueryParam("amount") BigDecimal amount) {
        UserEntity user = UserEntity.findById(accountNumber);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        try {
            user.updateBalance(action, amount);
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/transfer")
    @Transactional
    public Response transferBalance(TransferRequest transferRequest) {
        UserEntity source = UserEntity.findById(transferRequest.sourceAccountNumber());
        UserEntity target = UserEntity.findById(transferRequest.targetAccountNumber());

        if (source == null || target == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("One or both accounts missing.").build();
        }

        try {
            source.updateBalance("decrease", transferRequest.amount());
            target.updateBalance("increase", transferRequest.amount());
            return Response.ok("Transaction completed successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
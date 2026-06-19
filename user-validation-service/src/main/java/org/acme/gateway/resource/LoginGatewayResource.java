package org.acme.gateway.resource;

import org.acme.gateway.client.AuthLoginClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Frontend sends its Basic credentials here once at login time.
 * Success returns {"accountNumber": "...", "role": "..."} from the auth
 * service; bad credentials surface as 401.
 */
@Path("/gateway/login")
public class LoginGatewayResource {

    @Inject
    @RestClient
    AuthLoginClient authLoginClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Missing credentials\"}")
                    .build();
        }
        try {
            return Response.ok(authLoginClient.login(authHeader)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid account number or password\"}")
                    .build();
        }
    }
}

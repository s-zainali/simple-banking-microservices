package org.acme.gateway.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "auth-service-api")
@Path("/auth/login")
public interface AuthLoginClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    String login(@HeaderParam("Authorization") String authHeader);
}

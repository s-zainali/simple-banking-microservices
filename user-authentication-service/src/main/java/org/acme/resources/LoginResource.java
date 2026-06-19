package org.acme.resources;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Credential check endpoint. Quarkus' security-jpa layer performs the
 * actual Basic-auth validation against user_entities before this method
 * runs; if the credentials are wrong the caller gets a 401 and never
 * reaches the body. On success we just echo back who they are and what
 * role they hold so the frontend can build its menu.
 */
@Path("/auth/login")
public class LoginResource {

    @Inject
    SecurityIdentity identity;

    public record LoginResponse(String accountNumber, String role) {}

    @GET
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse login() {
        String role = identity.getRoles().stream().findFirst().orElse("UNKNOWN");
        return new LoginResponse(identity.getPrincipal().getName(), role);
    }
}

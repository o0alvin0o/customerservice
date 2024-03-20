package com.myth.client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@RegisterRestClient(configKey = "users-api")
@Path("/users")
@ClientHeaderParam(name = "Authorization", value = "Basic bWlrZWw6TWlrZWxAMjAyNA==")
public interface UserClient {

    @GET
    @Path("/{username}")
    Uni<RestResponse<Void>> isUserExist(@PathParam("username") String username);
}

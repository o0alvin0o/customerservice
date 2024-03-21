package com.myth.client;

import com.myth.dto.User;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@RegisterRestClient(configKey = "users-api")
@ClientHeaderParam(name = "Authorization", value = "Basic bWlrZWw6TWlrZWxAMjAyNA==")
public interface UserClient {

    @GET
    @Path("/users/{username}")
    Uni<RestResponse<Void>> isUserExist(@PathParam("username") String username);

    @GET
    @Path("/stress/random-user")
    @Consumes(MediaType.APPLICATION_JSON)
    Uni<User> getRandomUser();
}

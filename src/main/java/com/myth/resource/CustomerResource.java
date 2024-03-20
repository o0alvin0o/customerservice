package com.myth.resource;

import com.myth.dto.CustomerCreationDTO;
import com.myth.service.CustomerService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;

@Path("/customers")
public class CustomerResource {

    private static final Logger LOG = Logger.getLogger(CustomerResource.class);

    @Inject
    CustomerService customerService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<Object>> createCustomer(CustomerCreationDTO c) {
        return customerService.createCustomer(c)
                .onItem().transform(customer -> RestResponse.created(URI.create("/customers/" + customer.getId())))
                .onFailure().recoverWithItem(ex -> {
                    LOG.info(ex.getMessage()); return RestResponse.serverError();});
    }
}

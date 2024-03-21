package com.myth.resource;

import com.myth.dto.CustomerCreationDTO;
import com.myth.dto.User;
import com.myth.entity.Customer;
import com.myth.service.CustomerService;
import com.myth.service.DataGenerator;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
                    LOG.info(ex.getMessage()); return convertException(ex);});
    }

    @GET
    @Path("/stress-create")
    @WithSession
    public Uni<RestResponse<String>> stressCreateCustomer(@QueryParam("quantity") int quantity) {
        AtomicInteger count = new AtomicInteger();
        // Create a stream of indices to process each customer creation
        return Multi.createFrom().range(0, quantity)
                .onItem().transformToUniAndConcatenate(i ->
                        customerService.getRandomUsername()
                                .onItem().transformToUni(username ->
                                        customerService.numberOfUserAssociated(username)
                                                .onItem().transformToUni(size -> {
                                                    if (size < 2) {
                                                        return customerService.saveCustomer(DataGenerator.generateCustomerWithAddr(username))
                                                                .onItem().invoke(count::getAndIncrement)
                                                                .replaceWith(Uni.createFrom().voidItem()); // Proceed without altering the stream
                                                    }
                                                    return Uni.createFrom().voidItem(); // Skip creating a customer if condition is not met
                                                })
                                )
                ).collect().asList() // Collect results to proceed to final step
                .onItem().transform(ignored -> RestResponse.ok("Created: " + count.get() + " customers."))
                .onFailure().recoverWithUni(ex -> {
                    LOG.info(ex.getMessage()); return Uni.createFrom().item(RestResponse.ok(ex.getMessage()));
                });
    }

    private RestResponse<Object> convertException(Throwable e) {
        if (e instanceof WebApplicationException) {
            return RestResponse.notFound();
        } else {
            return RestResponse.serverError();
        }
    }

    @GET
    public Uni<RestResponse<List<Customer>>> getAll() {
        return customerService.getAllCustomer().onItem()
                .transform(RestResponse::ok);
    }
}

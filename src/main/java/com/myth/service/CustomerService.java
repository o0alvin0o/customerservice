package com.myth.service;

import com.myth.client.UserClient;
import com.myth.dto.CustomerCreationDTO;
import com.myth.dto.User;
import com.myth.entity.Customer;
import com.myth.repo.CustomerRepo;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class CustomerService {

    private static final Logger LOG = Logger.getLogger(CustomerService.class);

    @Inject
    CustomerRepo customerRepo;

    @RestClient
    UserClient userClient;

    @WithTransaction
    public Uni<Customer> createCustomer(CustomerCreationDTO c) {
        return userClient.isUserExist(c.username()).onItem()
                .transformToUni(Unchecked.function(response -> {
                    if (response.getStatus() == 200) {
                        Customer customer = new Customer();
                        customer.setFirstName(c.firstName());
                        customer.setLastName(c.lastName());
                        customer.setPhoneNumber(c.phoneNumber());
                        customer.setUsername(c.username());
                        return customerRepo.persist(customer);
                    } else {
                        throw new WebApplicationException("Failed to link customer", response.getStatus());
                    }
                }));

    }

    @WithTransaction
    public Uni<Customer> saveCustomer(Customer c) {
        return userClient.isUserExist(c.getUsername()).onItem()
                .transformToUni(Unchecked.function(response -> {
                    if (response.getStatus() == 200) {
                        return customerRepo.persist(c);
                    } else {
                        throw new WebApplicationException("Failed to link customer", response.getStatus());
                    }
                }));

    }

    @WithSession
    public Uni<String> getRandomUsername() {
        return userClient.getRandomUser().onItem()
                .transform(User::username);
    }

    @WithSession
    public Uni<Long> numberOfUserAssociated(String username) {
        return customerRepo.count("username", username);
    }

    @WithSession
    public Uni<List<Customer>> getAllCustomer() {
        return customerRepo.findAll().list();
    }
}

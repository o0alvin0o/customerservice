package com.myth.service;

import com.myth.entity.Address;
import com.myth.entity.Customer;
import com.myth.enums.AddressType;
import com.myth.enums.City;
import com.myth.enums.Nation;
import net.datafaker.Faker;

import java.util.List;
import java.util.Random;


public class DataGenerator {
    private static final Faker FAKER = new Faker();
    private static final Random RAND = new Random();

    public static Customer generateCustomerWithAddr(String username) {
        Customer c = new Customer();
        c.setUsername(username);
        c.setLastName(FAKER.name().lastName());
        c.setFirstName(FAKER.name().lastName());
        c.setPhoneNumber(FAKER.phoneNumber().phoneNumber());
        Address a1 = generateAddr();
        Address a2 = generateAddr();
        a1.setCustomer(c);
        a2.setCustomer(c);
        c.setAddresses(List.of(a1, a2));
        return c;
    }

    public static Address generateAddr() {
        Address a = new Address();
        a.setType(randomEnum(AddressType.class));
        a.setWard(FAKER.address().state());
        a.setCity(randomEnum(City.class));
        a.setDistrict(FAKER.address().postcode());
        a.setNation(randomEnum(Nation.class));
        a.setStreetNumber(FAKER.address().streetAddress());
        return a;
    }

    public static <E extends Enum<?>> E randomEnum(Class<E> eClass) {
        E[] enumConstants = eClass.getEnumConstants();
        int randomIndex = RAND.nextInt(enumConstants.length);
        return enumConstants[randomIndex];
    }
}

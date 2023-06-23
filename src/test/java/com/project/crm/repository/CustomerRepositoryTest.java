package com.project.crm.repository;

import com.project.crm.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CustomerRepositoryTest {

    private final CustomerRepository customerRepository;

    @Autowired
    CustomerRepositoryTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void ItShouldFindByFirstNameAndLastName() {
        String firstName = "Jan";
        String lastName = "Kowalski";
        Customer customer = new Customer(
                firstName,
                lastName,
                "jam.kowalski@gmail.com",
                "111 222 333",
                "01-500 Warszawa"
        );
        customerRepository.save(customer);

        List<Customer> result = customerRepository.findByFirstNameAndLastName(firstName, lastName);

        assertEquals(1, result.size());
        assertEquals(firstName, result.get(0).getFirstName());
        assertEquals(lastName, result.get(0).getLastName());

    }

    @Test
    void ItShouldNotFindByFirstNameAndLastName() {

        String firstName = "Jan";
        String lastName = "Kowalski";

        List<Customer> result = customerRepository.findByFirstNameAndLastName(firstName, lastName);

        assertEquals(0, result.size());
    }

    @Test
    void checkWhenCustomerEmailDoesExists() {
        String email = "piotr.rakocki@email.com";
        Customer customer = new Customer(
                "Piotr",
                "Rakocki",
                email,
                "123 456 789",
                "01-199 Warszawa"
        );
        customerRepository.save(customer);

        boolean expected = customerRepository.selectExistsEmail(email);
        assertThat(expected).isTrue();
    }

    @Test
    void checkWhenCustomerEmailDoesNotExists() {
        String email = "jamila@gamil.com";

        boolean expected = customerRepository.selectExistsEmail(email);

        assertThat(expected).isFalse();
    }

    @Test
    void count() {
        Customer customer1 = new Customer(
                "Jan",
                "Kowalski",
                "jan.kowalski@gmail.com",
                "123 456 789",
                "01-500 Warszawa"
        );
        Customer customer2 = new Customer(
                "John",
                "Doe",
                "john.doe@gmail.com",
                "123 456 789",
                "01-500 Warszawa"
        );
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        long count = customerRepository.count();
        assertEquals(2, count);

    }
}
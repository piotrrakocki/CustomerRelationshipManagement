package com.project.crm.model;

import com.project.crm.exception.BadRequestException;
import com.project.crm.exception.CustomerNotFoundException;
import com.project.crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerDataTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerData customerData;

    @BeforeEach
    void setUp() {
        customerData = new CustomerData(customerRepository);
    }

    @Test
    void canGetAllCustomers() {
        // when
        customerData.getAllCustomers();
        // then
        verify(customerRepository).findAll();
    }

    @Test
    void canAddCustomer() {
        // given
        Customer customer = new Customer(
                "Piotr",
                "Rakocki",
                "piotr.rakocki@email.com",
                "123 456 789",
                "01-199 Warszawa"
        );
        // when
        customerData.addCustomer(customer);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer).isEqualTo(customer);
    }

    @Test
    void willThrowWhenEmailIsTakenWhileAdding() {
        // given
        Customer customer = new Customer(
                "Piotr",
                "Rakocki",
                "piotr.rakocki@email.com",
                "123 456 789",
                "01-199 Warszawa"
        );
        given(customerRepository.selectExistsEmail(customer.getEmail()))
                .willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> customerData.addCustomer(customer))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Customer with the same email already exists.");
        verify(customerRepository, never()).save(any());
    }

    @Test
    void canRemoveCustomer() {
        // given
        long id = 10;
        given(customerRepository.existsById(id)).willReturn(true);
        // when
        customerData.removeCustomer(id);
        // then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void willThrowWhenCustomerDoesNotExists() {
        // given
        long id = 10;
        given(customerRepository.existsById(id)).willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> customerData.removeCustomer(id))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer with ID " + id + " does not exist.");

        verify(customerRepository, never()).deleteById(id);
    }

    @Test
    void canUpdateCustomer() {
        //given
        Customer customer = new Customer(
                "Piotr",
                "Rakocki",
                "piotr.rakocki@email.com",
                "123 456 789",
                "01-199 Warszawa"
        );
        // when
        customerData.updateCustomer(customer);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer).isEqualTo(customer);
    }

    @Test
    void canGetCustomerById() {
        // given
        long id = 1;
        Customer customer = new Customer(
                "Piotr",
                "Rakocki",
                "piotr.rakocki@email,com",
                "123 456 789",
                "01-100 Pułtusk"
        );
        customer.setId(id);
        // when
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        Customer result = customerData.getCustomer(id);
        // then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Piotr", result.getFirstName());
        assertEquals("Rakocki", result.getLastName());
    }

    @Test
    void canGetCustomerByFirstNameAndLastName() {
        // given
        String firstName = "Piotr";
        String lastName = "Rakocki";
        Customer customer = new Customer(
                firstName,
                lastName,
                "piotr.rakocki@email,com",
                "123 456 789",
                "01-100 Pułtusk"
        );
        customerRepository.save(customer);
        // when
        when(customerRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Collections.singletonList(customer));
        List<Customer> result = customerData.getCustomer(firstName, lastName);
        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(customer);
    }

    @Test
    void getNumberOfCustomers() {
        // given
        long expectedCount = 10;
        // when
        when(customerRepository.count()).thenReturn(expectedCount);
        long result = customerData.getNumberOfCustomers();
        // then
        assertEquals(expectedCount, result);

    }
}
package com.project.crm.model;

import com.project.crm.exception.BadRequestException;
import com.project.crm.exception.CustomerNotFoundException;
import com.project.crm.repository.CustomerRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerData {

    private final CustomerRepository customerRepository;

    private static long idValue = 1;

    @Autowired
    public CustomerData(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void addCustomer(@NonNull Customer customer) {
        Boolean existsEmail = customerRepository.selectExistsEmail(customer.getEmail());

        if (existsEmail) {
            throw new BadRequestException("Customer with the same email already exists.");
        }
        customer.setId(idValue);
        customerRepository.save(customer);
        idValue++;
    }

    public void removeCustomer(long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        } else {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
    }

    public void updateCustomer(@NonNull Customer toUpdate) {
        customerRepository.save(toUpdate);
    }

    public Customer getCustomer(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> getCustomer(String firstName, String lastName) {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName);
   }

    public long getNumberOfCustomers() {
        return customerRepository.count();
    }
}

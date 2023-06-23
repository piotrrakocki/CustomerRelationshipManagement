package com.project.crm.service;

import com.project.crm.model.Customer;
import com.project.crm.model.CustomerData;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Getter
    private final CustomerData customerData;

    @Autowired
    public CustomerServiceImpl(CustomerData customerData) {
        this.customerData = customerData;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerData.getAllCustomers();
    }

    @Override
    public void addCustomer(Customer customer) {
        customerData.addCustomer(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerData.updateCustomer(customer);
    }

    @Override
    public void removeCustomer(long id) {
        customerData.removeCustomer(id);
    }

    @Override
    public List<Customer> getCustomer(String firstName, String lastName) {
        return customerData.getCustomer(firstName, lastName);
    }

    @Override
    public Customer getCustomer(long id) {
        return customerData.getCustomer(id);
    }

    @Override
    public long getNumberOfCustomers() {
        return customerData.getNumberOfCustomers();
    }
}
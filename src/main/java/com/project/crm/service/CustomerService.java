package com.project.crm.service;

import com.project.crm.model.Customer;
import com.project.crm.model.CustomerData;

import java.util.List;

public interface CustomerService {

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void removeCustomer(long id);

    Customer getCustomer(long id);

    List<Customer> getCustomer(String firstName, String lastName);

    CustomerData getCustomerData();

    @SuppressWarnings("unused")
    long getNumberOfCustomers();

    List<Customer> getAllCustomers();
}

package com.project.crm.controller;

import com.project.crm.model.Customer;
import com.project.crm.model.CustomerData;
import com.project.crm.repository.CustomerRepository;
import com.project.crm.service.CustomerService;
import com.project.crm.util.AttributeNames;
import com.project.crm.util.Mappings;
import com.project.crm.util.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class CrmController {

    public final CustomerService customerService;

    public CustomerRepository customerRepository;

    @Autowired
    public CrmController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @ModelAttribute("customerData")
    public CustomerData customerData() {
        return customerService.getCustomerData();
    }

    @GetMapping(Mappings.CUSTOMERS)
    public String customers(@RequestParam(required = false) String searchTerm, Model model) {
        log.info("searchTerm: " + searchTerm);
        List<Customer> customers;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            String[] searchTerms = searchTerm.split(" ");
            String firstName;
            String lastName;
            if (searchTerms.length >= 2) {
                firstName = searchTerms[0];
                lastName = searchTerms[1];
            } else if (searchTerms.length == 1) {
                firstName = searchTerms[0];
                lastName = "";
                customers = customerService.getCustomer(firstName, lastName);
                if (customers.isEmpty()) {
                    firstName = "";
                    lastName = searchTerms[0];
                }
            } else {
                return "search-customers";
            }
            customers = customerService.getCustomer(firstName, lastName);
        } else {
            customers = customerService.getAllCustomers();
        }
        model.addAttribute("customers", customers);
        return ViewNames.CUSTOMER_LIST;
    }


    @GetMapping(Mappings.REPORTS)
    public String reports() {
        return ViewNames.REPORTS;
    }

    @GetMapping(Mappings.ADD_CUSTOMER)
    public String addEditCustomer(@RequestParam(required = false, defaultValue = "-1") int id, Model model) {
        log.info("editing id = {}", id);
        Customer customer = customerService.getCustomer(id);
        if (customer == null) {
            customer = new Customer("", "", "", "", "");
        }

        model.addAttribute(AttributeNames.CUSTOMER, customer);
        model.addAttribute("error", model.getAttribute("error"));
        return ViewNames.ADD_CUSTOMER;
    }

    @PostMapping(Mappings.ADD_CUSTOMER)
    public String processCustomer(@ModelAttribute(AttributeNames.CUSTOMER) Customer customer, Model model) {
        log.info("customer from form = {}", customer);
        try {
            if (customer.getId() == 0) {
                customerService.addCustomer(customer);
            } else {
                customerService.updateCustomer(customer);
            }
            return "redirect:/" + Mappings.CUSTOMERS;
        } catch (IllegalArgumentException e) {
            model.addAttribute(AttributeNames.CUSTOMER, customer);
            model.addAttribute("error", "Email is already taken.");
            return ViewNames.ADD_CUSTOMER;
        }
    }

    @GetMapping(Mappings.DELETE_CUSTOMER)
    public String deleteCustomer(@RequestParam int id) {
        log.info("Deleting customer with id = {}", id);
        customerService.removeCustomer(id);
        return "redirect:/" + Mappings.CUSTOMERS;
    }

    @GetMapping(Mappings.VIEW_CUSTOMER)
    public String viewCustomer(@RequestParam int id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute(AttributeNames.CUSTOMER, customer);
        return ViewNames.VIEW_CUSTOMER;
    }
}

package com.project.crm.util;

import org.springframework.stereotype.Component;

@Component
public final class Mappings {

    public static final String CUSTOMERS = "customers";
    public static final String ADD_CUSTOMER = "addCustomer";
    public static final String DELETE_CUSTOMER = "delete_customer";
    public static final String VIEW_CUSTOMER = "viewCustomer";
    public static final String REPORTS = "reports";

    private Mappings() {}
}

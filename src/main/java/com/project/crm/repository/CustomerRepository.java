package com.project.crm.repository;

import com.project.crm.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.firstName LIKE %:firstName% AND c.lastName LIKE %:lastName%")
    List<Customer> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.email LIKE :email")
    Boolean selectExistsEmail(@Param("email") String email);

    long count();
}

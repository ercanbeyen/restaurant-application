package com.ercanbeyen.restaurantapplication.repository;

import com.ercanbeyen.restaurantapplication.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByFullName(String fullName);
}

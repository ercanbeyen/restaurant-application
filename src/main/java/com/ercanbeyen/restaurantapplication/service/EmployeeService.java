package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    void createEmployee(EmployeeDto request);
    void updateEmployee(String id, EmployeeDto request);
    List<EmployeeDto> getEmployees();
    EmployeeDto getEmployee(String id);
    void deleteEmployee(String id);
}

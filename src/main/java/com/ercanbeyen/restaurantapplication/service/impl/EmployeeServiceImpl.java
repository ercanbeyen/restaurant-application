package com.ercanbeyen.restaurantapplication.service.impl;

import com.ercanbeyen.restaurantapplication.dto.EmployeeDto;
import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
import com.ercanbeyen.restaurantapplication.mapper.EmployeeMapper;
import com.ercanbeyen.restaurantapplication.model.Employee;
import com.ercanbeyen.restaurantapplication.repository.EmployeeRepository;
import com.ercanbeyen.restaurantapplication.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEE_IS_NOT_FOUND = "Employee is not found";
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public void createEmployee(EmployeeDto request) {
        Employee employee = employeeMapper.dtoToEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee {} is successfully created", savedEmployee.getId());
    }

    @Override
    public void updateEmployee(String id, EmployeeDto request) {
        Employee employee = findById(id);
        employee.setFullName(request.fullName());

        employeeRepository.save(employee);

        log.info("Employee {} is successfully updated", id);
    }

    @Override
    public List<EmployeeDto> getEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::entityToDto)
                .toList();
    }

    @Override
    public EmployeeDto getEmployee(String id) {
        return employeeMapper.entityToDto(findById(id));
    }

    @Override
    public void deleteEmployee(String id) {
        employeeRepository.findById(id)
                .ifPresentOrElse(
                        employeeRepository::delete
                        , () -> {
                            log.error("Employee {} is not found", id);
                            throw new NotFoundException(EMPLOYEE_IS_NOT_FOUND);
                        }
                );
    }

    @Override
    public Employee findByFullName(String fullName) {
        return employeeRepository.findByFullName(fullName)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_IS_NOT_FOUND));
    }

    private Employee findById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_IS_NOT_FOUND));
    }
}

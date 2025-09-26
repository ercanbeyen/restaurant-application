package com.ercanbeyen.restaurantapplication.service.impl;

import com.ercanbeyen.restaurantapplication.constant.enums.Model;
import com.ercanbeyen.restaurantapplication.constant.message.ErrorMessage;
import com.ercanbeyen.restaurantapplication.constant.message.LogMessage;
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
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public void createEmployee(EmployeeDto request) {
        Employee employee = employeeMapper.dtoToEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info(LogMessage.SUCCESSFULLY_CREATED, Model.EMPLOYEE.getValue(), savedEmployee.getId());
    }

    @Override
    public void updateEmployee(String id, EmployeeDto request) {
        Employee employee = findById(id);
        employee.setFullName(request.fullName());

        employeeRepository.save(employee);

        log.info(LogMessage.SUCCESSFULLY_UPDATED, Model.EMPLOYEE.getValue(), id);
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
        String entity = Model.EMPLOYEE.getValue();

        employeeRepository.findById(id)
                .ifPresentOrElse(
                        employeeRepository::delete
                        , () -> {
                            log.error(LogMessage.NOT_FOUND, entity, id);
                            throw new NotFoundException(String.format(ErrorMessage.NOT_FOUND, entity));
                        }
                );

        log.info(LogMessage.SUCCESSFULLY_DELETED, entity, id);
    }

    @Override
    public Employee findByFullName(String fullName) {
        return employeeRepository.findByFullName(fullName)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessage.NOT_FOUND, Model.EMPLOYEE.getValue())));
    }

    private Employee findById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessage.NOT_FOUND, Model.EMPLOYEE.getValue())));
    }
}

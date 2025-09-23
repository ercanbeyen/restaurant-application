package com.ercanbeyen.restaurantapplication.mapper;

import com.ercanbeyen.restaurantapplication.dto.EmployeeDto;
import com.ercanbeyen.restaurantapplication.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDto entityToDto(Employee employee);
    Employee dtoToEntity(EmployeeDto employeeDto);
}

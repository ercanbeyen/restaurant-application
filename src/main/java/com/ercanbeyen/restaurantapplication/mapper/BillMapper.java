package com.ercanbeyen.restaurantapplication.mapper;

import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.model.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillMapper {
    @Mapping(target = "employeeFullName", source = "employee.fullName")
    BillDto entityToDto(Bill bill);
    Bill dtoToEntity(BillDto billDto);
}

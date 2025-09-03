package com.ercanbeyen.restaurantapplication.mapper;

import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.model.Bill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillMapper {
    BillDto entityToDto(Bill bill);
    Bill dtoToEntity(BillDto billDto);
}

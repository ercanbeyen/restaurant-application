package com.ercanbeyen.restaurantapplication.mapper;

import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import com.ercanbeyen.restaurantapplication.model.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto entityToDto(Item item);
    Item dtoToEntity(ItemDto itemDto);
}

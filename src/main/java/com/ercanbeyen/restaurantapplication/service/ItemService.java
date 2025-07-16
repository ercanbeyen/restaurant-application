package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import org.springframework.data.domain.Page;

public interface ItemService {
    void createItem(ItemDto request);
    void updateItem(Long id, ItemDto request);
    ItemDto getItem(Long id);
    Page<ItemDto> getItems(int pageNumber, int pageSize);
    void deleteItem(Long id);
}

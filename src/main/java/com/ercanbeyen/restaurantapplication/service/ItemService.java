package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.dto.ItemDto;

import java.util.List;

public interface ItemService {
    void createItem(ItemDto request);
    void updateItem(Long id, ItemDto request);
    ItemDto getItem(Long id);
    List<ItemDto> getItems();
    void deleteItem(Long id);
}

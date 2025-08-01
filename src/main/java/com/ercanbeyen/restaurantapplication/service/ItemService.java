package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ItemService {
    void createItem(ItemDto request);
    void updateItem(Long id, ItemDto request);
    ItemDto getItem(Long id);
    Page<ItemDto> getItems(String category, String sortField, String sortDirection, int pageNumber, int pageSize);
    void deleteItem(Long id);
    List<ItemDto> searchItems(String name);
}

package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.model.Item;

import java.util.List;

public interface ItemService {
    void createItem(Item request);
    void updateItem(Long id, Item request);
    Item getItem(Long id);
    List<Item> getItems();
    void deleteItem(Long id);
}

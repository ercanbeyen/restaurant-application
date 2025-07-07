package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.model.Item;

public interface ItemService {
    void createItem(Item request);
    void updateItem(Long id, Item request);
    Item getItem(Long id);
    void deleteItem(Long id);
}

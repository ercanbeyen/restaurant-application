package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.model.Item;

public interface ItemService {
    Item createItem(Item request);
    Item updateItem(Long id, Item request);
    Item getItem(Long id);
}

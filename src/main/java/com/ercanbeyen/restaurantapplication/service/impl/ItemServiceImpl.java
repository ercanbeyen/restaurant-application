package com.ercanbeyen.restaurantapplication.service.impl;


import com.ercanbeyen.restaurantapplication.model.Item;
import com.ercanbeyen.restaurantapplication.repository.ItemRepository;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item createItem(Item request) {
        return itemRepository.save(request);
    }
}

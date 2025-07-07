package com.ercanbeyen.restaurantapplication.service.impl;


import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
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

    @Override
    public Item updateItem(Long id, Item request) {
        Item item = findById(id);

        item.setName(request.getName());
        item.setCategory(request.getCategory());
        item.setPrice(request.getPrice());

        return itemRepository.save(item);
    }

    @Override
    public Item getItem(Long id) {
        return findById(id);
    }

    private Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item is not found"));
    }
}

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
    public void createItem(Item request) {
        Item item = itemRepository.save(request);
        log.info("Item {} is successfully created", item.getId());
    }

    @Override
    public void updateItem(Long id, Item request) {
        Item item = findById(id);

        item.setName(request.getName());
        item.setCategory(request.getCategory());
        item.setPrice(request.getPrice());

        itemRepository.save(item);

        log.info("Item {} is successfully updated", item.getId());
    }

    @Override
    public Item getItem(Long id) {
        return findById(id);
    }

    @Override
    public void deleteItem(Long id) {
        Item item = findById(id);
        itemRepository.delete(item);

        log.info("Item {} is successfully deleted", id);
    }

    private Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item is not found"));
    }
}

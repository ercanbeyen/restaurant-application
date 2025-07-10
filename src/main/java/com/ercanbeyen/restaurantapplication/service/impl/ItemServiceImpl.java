package com.ercanbeyen.restaurantapplication.service.impl;


import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
import com.ercanbeyen.restaurantapplication.mapper.ItemMapper;
import com.ercanbeyen.restaurantapplication.model.Item;
import com.ercanbeyen.restaurantapplication.repository.ItemRepository;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public void createItem(ItemDto request) {
        Item item = itemMapper.dtoToEntity(request);
        Item savedItem = itemRepository.save(item);
        log.info("Item {} is successfully created", savedItem.getId());
    }

    @Override
    public void updateItem(Long id, ItemDto request) {
        Item item = findById(id);

        item.setName(request.name());
        item.setCategory(request.category());
        item.setPrice(request.price());

        itemRepository.save(item);

        log.info("Item {} is successfully updated", item.getId());
    }

    @Override
    public ItemDto getItem(Long id) {
        return itemMapper.entityToDto(findById(id));
    }

    @Override
    public List<ItemDto> getItems() {
        return itemRepository.findAll()
                .stream()
                .map(itemMapper::entityToDto)
                .toList();
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

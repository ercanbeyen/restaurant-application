package com.ercanbeyen.restaurantapplication.service.impl;


import com.ercanbeyen.restaurantapplication.constant.enums.ItemCategory;
import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
import com.ercanbeyen.restaurantapplication.mapper.ItemMapper;
import com.ercanbeyen.restaurantapplication.model.Item;
import com.ercanbeyen.restaurantapplication.repository.ItemRepository;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        item.setCategory(ItemCategory.valueOf(request.category()));
        item.setPrice(request.price());

        itemRepository.save(item);

        log.info("Item {} is successfully updated", item.getId());
    }

    @Override
    public ItemDto getItem(Long id) {
        return itemMapper.entityToDto(findById(id));
    }

    @Override
    public Page<ItemDto> getItems(String itemCategory, String sortField, String sortDirection, int pageNumber, int pageSize) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        ItemCategory category = Enum.valueOf(ItemCategory.class, itemCategory);

        return itemRepository.findByCategory(category, pageable)
                .map(itemMapper::entityToDto);
    }

    @Override
    public String deleteItem(Long id) {
        Item item = findById(id);
        String category = String.valueOf(item.getCategory());
        itemRepository.delete(item);

        log.info("Item {} is successfully deleted", id);
        return category;
    }

    @Override
    public List<ItemDto> searchItems(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(itemMapper::entityToDto)
                .toList();
    }

    @Override
    public ItemDto getItemByName(String name) {
        Item item = itemRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Item is not found"));
        return itemMapper.entityToDto(item);
    }

    private Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item is not found"));
    }
}

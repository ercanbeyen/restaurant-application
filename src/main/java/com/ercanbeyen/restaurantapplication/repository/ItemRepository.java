package com.ercanbeyen.restaurantapplication.repository;

import com.ercanbeyen.restaurantapplication.constant.enums.ItemCategory;
import com.ercanbeyen.restaurantapplication.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByCategory(ItemCategory itemCategory, Pageable pageable);
}

package com.ercanbeyen.restaurantapplication.repository;

import com.ercanbeyen.restaurantapplication.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}

package com.ercanbeyen.restaurantapplication.repository;

import com.ercanbeyen.restaurantapplication.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
    Optional<Bill> findByTableNumber(Integer tableNumber);
}

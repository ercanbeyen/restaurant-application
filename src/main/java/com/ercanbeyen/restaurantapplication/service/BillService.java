package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.model.Bill;
import com.ercanbeyen.restaurantapplication.model.Order;

import java.util.List;

public interface BillService {
    BillDto createBill(BillDto request);
    BillDto getBill(Integer tableNumber);
    List<BillDto> getBills();
    void deleteBill(Integer tableNumber);
    BillDto addOrder(Integer tableNumber, Order order);
    BillDto updateOrder(Integer tableNumber, Order order);
    BillDto deleteOrder(Integer tableNumber, String itemName);
    Bill findByTableNumber(Integer tableNumber);
}

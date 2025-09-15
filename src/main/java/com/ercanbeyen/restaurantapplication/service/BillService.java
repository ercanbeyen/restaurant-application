package com.ercanbeyen.restaurantapplication.service;

import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.model.Order;

import java.util.List;

public interface BillService {
    BillDto openBill(BillDto request);
    BillDto getBill(Integer tableNumber);
    List<BillDto> getBills();
    void closeBill(Integer tableNumber);
    BillDto addOrder(Integer tableNumber, Order order);
    BillDto updateOrder(Integer tableNumber, Order order);
    BillDto deleteOrder(Integer tableNumber, String itemName);
    Double calculateSum(BillDto request);
}

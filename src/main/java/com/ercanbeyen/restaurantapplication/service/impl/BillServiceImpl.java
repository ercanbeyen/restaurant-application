package com.ercanbeyen.restaurantapplication.service.impl;

import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.exception.AlreadyExistsException;
import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
import com.ercanbeyen.restaurantapplication.mapper.BillMapper;
import com.ercanbeyen.restaurantapplication.model.Bill;
import com.ercanbeyen.restaurantapplication.model.Order;
import com.ercanbeyen.restaurantapplication.repository.BillRepository;
import com.ercanbeyen.restaurantapplication.service.BillService;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final ItemService itemService;

    @Override
    public BillDto createBill(BillDto request) {
        boolean isTableFilled = billRepository.findByTableNumber(request.tableNumber())
                .isPresent();

        if (isTableFilled) {
            throw new AlreadyExistsException("Bill has already been created for the table");
        }

        Bill bill = billMapper.dtoToEntity(request);
        return billMapper.entityToDto(billRepository.save(bill));
    }

    @Override
    public BillDto getBill(Integer tableNumber) {
        Bill bill = findByTableNumber(tableNumber);
        return billMapper.entityToDto(bill);
    }

    @Override
    public List<BillDto> getBills() {
        return billRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Bill::getTableNumber))
                .map(billMapper::entityToDto)
                .toList();
    }

    @Transactional
    @Override
    public BillDto addOrder(Integer tableNumber, Order order) {
        Bill bill = findByTableNumber(tableNumber);

        for (Order orderInBill : bill.getOrders()) {
            if (orderInBill.getItemName().equals(order.getItemName())) {
                log.error("Item has already been in the bill");
                throw new AlreadyExistsException("Item is in the bill. Update the amount");
            }
        }

        bill.getOrders().add(order);
        return billMapper.entityToDto(billRepository.save(bill));
    }

    @Transactional
    @Override
    public BillDto updateOrder(Integer tableNumber, Order order) {
        Bill bill = findByTableNumber(tableNumber);

        for (Order orderInBill : bill.getOrders()) {
            if (orderInBill.getItemName().equals(order.getItemName())) {
                orderInBill.setAmount(order.getAmount());
                break;
            }
        }

        return billMapper.entityToDto(billRepository.save(bill));
    }

    @Transactional
    @Override
    public BillDto deleteOrder(Integer tableNumber, String itemName) {
        Bill bill = findByTableNumber(tableNumber);
        Order order = new Order();

        for (Order orderInBill : bill.getOrders()) {
            if (orderInBill.getItemName().equals(itemName)) {
                order = orderInBill;
                break;
            }
        }

        bill.getOrders().remove(order);
        return billMapper.entityToDto(billRepository.save(bill));
    }

    @Override
    public Bill findByTableNumber(Integer tableNumber) {
        return billRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> new NotFoundException("Bill is not found"));
    }
}

package com.ercanbeyen.restaurantapplication.service.impl;

import com.ercanbeyen.restaurantapplication.constant.enums.Model;
import com.ercanbeyen.restaurantapplication.constant.message.ErrorMessage;
import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import com.ercanbeyen.restaurantapplication.exception.AlreadyExistsException;
import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
import com.ercanbeyen.restaurantapplication.mapper.BillMapper;
import com.ercanbeyen.restaurantapplication.model.Bill;
import com.ercanbeyen.restaurantapplication.model.Employee;
import com.ercanbeyen.restaurantapplication.model.Order;
import com.ercanbeyen.restaurantapplication.repository.BillRepository;
import com.ercanbeyen.restaurantapplication.service.BillService;
import com.ercanbeyen.restaurantapplication.service.EmployeeService;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final ItemService itemService;
    private final EmployeeService employeeService;

    @Override
    public BillDto openBill(BillDto request) {
        boolean isTableFilled = billRepository.findByTableNumber(request.tableNumber())
                .isPresent();

        if (isTableFilled) {
            throw new AlreadyExistsException(String.format("%s has already been opened for the table", Model.BILL.getValue()));
        }

        Bill bill = billMapper.dtoToEntity(request);
        Employee employee = employeeService.findByFullName(request.employeeFullName());
        bill.setEmployee(employee);
        LocalDateTime now = LocalDateTime.now();
        bill.setOpenDate(now);
        bill.setUpdateDate(now);
        return billMapper.entityToDto(billRepository.save(bill));
    }

    @Override
    public BillDto getBill(Integer tableNumber) {
        return billMapper.entityToDto(findByTableNumber(tableNumber));
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
    public void closeBill(Integer tableNumber) {
        String model = Model.BILL.getValue();

        billRepository.findByTableNumber(tableNumber)
                .ifPresentOrElse(
                        _ -> billRepository.deleteByTableNumber(tableNumber)
                        , () -> {
                            log.error("{} with table number {} is not found", model, tableNumber);
                            throw new NotFoundException(String.format(ErrorMessage.NOT_FOUND, model));
                        });

        log.info("{} with table number {} is successfully closed", model, tableNumber);
    }

    @Transactional
    @Override
    public BillDto addOrder(Integer tableNumber, Order order) {
        Bill bill = findByTableNumber(tableNumber);

        for (Order orderInBill : bill.getOrders()) {
            if (orderInBill.getItemName().equals(order.getItemName())) {
                String itemModel = Model.ITEM.getValue();
                String billModel = Model.BILL.getValue();
                log.error("{} has already been in the {}", itemModel, billModel);
                throw new AlreadyExistsException(String.format("%s is in the %s. Update the amount", itemModel, billModel));
            }
        }

        bill.getOrders().add(order);
        bill.setUpdateDate(LocalDateTime.now());
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

        bill.setUpdateDate(LocalDateTime.now());
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
        bill.setUpdateDate(LocalDateTime.now());
        return billMapper.entityToDto(billRepository.save(bill));
    }

    @Override
    public Double calculateSum(BillDto request) {
        double sum = 0;

        for (Order order : request.orders()) {
            ItemDto itemDto = itemService.getItemByName(order.getItemName());
            double cost = order.getAmount() * itemDto.price();
            sum += cost;
        }

        return sum;
    }

    private Bill findByTableNumber(Integer tableNumber) {
        return billRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessage.NOT_FOUND, Model.BILL.getValue())));
    }
}

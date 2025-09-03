package com.ercanbeyen.restaurantapplication.controller;

import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.model.Bill;
import com.ercanbeyen.restaurantapplication.model.Order;
import com.ercanbeyen.restaurantapplication.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BillController {
    private final BillService billService;

    @GetMapping("/showCreateBillForm")
    public String showCreateBillForm(Bill bill) {
        return "create-bill";
    }

    @GetMapping("/showAddOrderForm/tables/{tableNumber}")
    public String showAddOrderForm(@PathVariable("tableNumber") Integer tableNumber, Model model) {
        Order order = new Order();
        model.addAttribute("order", order);
        model.addAttribute("tableNumber", tableNumber);
        return "add-order";
    }

    @GetMapping("/showUpdateOrderForm/tables/{tableNumber}/orders/{itemName}")
    public String showUpdateOrderForm(@PathVariable("tableNumber") Integer tableNumber, @PathVariable("itemName") String itemName, Model model) {
        Order order = new Order();
        order.setItemName(itemName);
        model.addAttribute("order", order);
        model.addAttribute("tableNumber", tableNumber);
        return "update-order";
    }

    @PostMapping("/createBill")
    public String createBill(@Valid @ModelAttribute("bill") BillDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-bill";
        }

        BillDto response = billService.createBill(request);
        return callGetBill(response.tableNumber());
    }

    @GetMapping("/getBill/tables/{tableNumber}")
    public String getBill(@PathVariable("tableNumber") Integer tableNumber, Model model) {
        BillDto response = billService.getBill(tableNumber);
        model.addAttribute("bill", response);
        return "get-bill";
    }

    /*@GetMapping("/getBills")
    public String getBills(Model model) {
        return
    }*/

    @PostMapping("/addOrder/tables/{tableNumber}")
    public String addOrder(@PathVariable("tableNumber") Integer tableNumber, Order order) {
        BillDto response = billService.addOrder(tableNumber, order);
        return callGetBill(response.tableNumber());
    }

    @PostMapping("/updateOrder/tables/{tableNumber}/orders/{itemName}")
    public String updateOrder(@PathVariable("tableNumber") Integer tableNumber, @PathVariable("itemName") String itemName, @Valid @ModelAttribute("order") Order order) {
        BillDto response = billService.updateOrder(tableNumber, order);
        return callGetBill(response.tableNumber());
    }

    @GetMapping("/deleteOrder/tables/{tableNumber}/orders/{itemName}")
    public String deleteOrder(@PathVariable("tableNumber") Integer tableNumber, @PathVariable("itemName") String itemName) {
        BillDto response = billService.deleteOrder(tableNumber, itemName);
        return callGetBill(response.tableNumber());
    }

    @GetMapping("/bill-management")
    public String showBillManagementPage(Model model) {
        List<BillDto> response = billService.getBills();
        model.addAttribute("bills", response);
        return "bill-management";
    }

    private String callGetBill(Integer tableNumber) {
        return String.format("redirect:/getBill/tables/%d", tableNumber);
    }
}

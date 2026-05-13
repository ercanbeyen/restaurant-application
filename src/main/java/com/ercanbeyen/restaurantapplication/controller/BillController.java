package com.ercanbeyen.restaurantapplication.controller;

import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.dto.EmployeeDto;
import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import com.ercanbeyen.restaurantapplication.exporter.PDFExporter;
import com.ercanbeyen.restaurantapplication.model.Bill;
import com.ercanbeyen.restaurantapplication.model.Order;
import com.ercanbeyen.restaurantapplication.service.BillService;
import com.ercanbeyen.restaurantapplication.service.EmployeeService;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import com.itextpdf.text.DocumentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/bills")
public class BillController {
    private final BillService billService;
    private final ItemService itemService;
    private final EmployeeService employeeService;

    @GetMapping("/showOpenBillForm")
    public String showOpenBillForm(Bill bill, Model model) {
        List<EmployeeDto> requestedEmployees = employeeService.getEmployees();
        model.addAttribute("employees", requestedEmployees);

        return "open-bill";
    }

    @GetMapping("/showUpdateBillForm/tables/{tableNumber}")
    public String showUpdateBillForm(@PathVariable("tableNumber") Integer tableNumber, Model model) {
        BillDto requestedBill =  billService.getBill(tableNumber);
        List<EmployeeDto> requestedEmployees = employeeService.getEmployees();

        model.addAttribute("bill", requestedBill);
        model.addAttribute("employees", requestedEmployees);

        return "update-bill";
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

    @PostMapping("/openBill")
    public String openBill(@Valid @ModelAttribute("bill") BillDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "open-bill";
        }

        BillDto response = billService.openBill(request);

        return callGetBill(response.tableNumber());
    }

    @GetMapping("/getBill/tables/{tableNumber}")
    public String getBill(@PathVariable("tableNumber") Integer tableNumber, Model model) {
        BillDto response = billService.getBill(tableNumber);
        model.addAttribute("bill", response);

        Double sum = billService.calculateSum(response);
        model.addAttribute("sum", sum);

        return "get-bill";
    }

    @PostMapping("/updateBill/tables/{tableNumber}")
    public String updateBill(@PathVariable("tableNumber") Integer tableNumber, @Valid @ModelAttribute("bill") BillDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update-bill";
        }

        BillDto response = billService.updateBill(tableNumber, request);

        return callGetBill(response.tableNumber());
    }

    @GetMapping("/generateBill/tables/{tableNumber}")
    public ResponseEntity<byte[]> generateBill(@PathVariable("tableNumber") Integer tableNumber) throws DocumentException, IOException {
        BillDto bill = billService.getBill(tableNumber);
        Map<String, Double> items = new HashMap<>();

        for (Order order : bill.orders()) {
            ItemDto itemDto = itemService.getItemByName(order.getItemName());
            items.put(itemDto.name(), itemDto.price());
        }

        ByteArrayOutputStream byteArrayOutputStream = PDFExporter.generatePDFStreamOfBillPaper(bill, items);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(byteArrayOutputStream.size());
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("table_" + tableNumber + "_bill.pdf")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(byteArrayOutputStream.toByteArray());
    }

    @GetMapping("/closeBill/tables/{tableNumber}")
    public String closeBill(@PathVariable("tableNumber") Integer tableNumber) {
        billService.closeBill(tableNumber);
        return "redirect:/bills/bill-management";
    }

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
        return String.format("redirect:/bills/getBill/tables/%d", tableNumber);
    }
}

package com.ercanbeyen.restaurantapplication.controller;

import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import com.ercanbeyen.restaurantapplication.model.Item;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/createItemForm")
    public String showCreateItemForm(Item item) {
        return "create-item";
    }

    @PostMapping("/createItem")
    public String createItem(@Valid @ModelAttribute("item") ItemDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-item";
        }

        itemService.createItem(request);
        return "redirect:/success";
    }

    @GetMapping("/updateItemForm/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        ItemDto request = itemService.getItem(id);
        model.addAttribute("item", request);
        return "update-item";
    }

    @PostMapping("updateItem/{id}")
    public String updateItem(@PathVariable("id") Long id, @Valid @ModelAttribute("item") ItemDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update-item";
        }

        itemService.updateItem(id, request);
        return "redirect:/success";
    }

    @GetMapping("/getItem/{id}")
    public String getItem(@PathVariable("id") Long id, Model model) {
        ItemDto itemDto = itemService.getItem(id);
        model.addAttribute("item", itemDto);
        return "get-item";
    }

    @GetMapping("/getItems")
    public String getItems(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "category") String itemCategory,
            Model model) {
        Page<ItemDto> page = itemService.getItems(itemCategory, pageNumber, pageSize);
        List<ItemDto> content = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("category", itemCategory);
        model.addAttribute("items", content);

        return "get-items";
    }

    @GetMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable("id") Long id, Model model) {
        itemService.deleteItem(id);
        model.addAttribute("id", id);
        return "success";
    }

    @GetMapping("/menu")
    String showMenuPage() {
        return "menu";
    }

    @GetMapping("/success")
    String showSuccessPage() {
        return "success";
    }
}

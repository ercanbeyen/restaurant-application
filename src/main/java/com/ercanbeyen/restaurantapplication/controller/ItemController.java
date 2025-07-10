package com.ercanbeyen.restaurantapplication.controller;

import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import com.ercanbeyen.restaurantapplication.model.Item;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String getItems(Model model) {
        List<ItemDto> itemDtos = itemService.getItems();
        model.addAttribute("items", itemDtos);
        return "get-items";
    }

    @GetMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable("id") Long id, Model model) {
        itemService.deleteItem(id);
        model.addAttribute("id", id);
        return "success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }
}

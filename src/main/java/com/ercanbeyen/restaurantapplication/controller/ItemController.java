package com.ercanbeyen.restaurantapplication.controller;

import com.ercanbeyen.restaurantapplication.model.Item;
import com.ercanbeyen.restaurantapplication.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String createItem(@ModelAttribute("item") Item item) {
        itemService.createItem(item);
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }
}

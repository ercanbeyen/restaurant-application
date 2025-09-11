package com.ercanbeyen.restaurantapplication.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {
    SALAD("Salad"),
    APPETIZER("Appetizer"),
    BEVERAGE("Beverage"),
    DESSERT("Dessert");

    private final String value;
}

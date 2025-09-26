package com.ercanbeyen.restaurantapplication.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Model {
    ITEM("Item"),
    BILL("Bill"),
    EMPLOYEE("Employee");

    private final String value;
}

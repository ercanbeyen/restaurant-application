package com.ercanbeyen.restaurantapplication.dto;

import com.ercanbeyen.restaurantapplication.constant.message.ErrorMessage;
import com.ercanbeyen.restaurantapplication.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BillDto(
        String id,
        @NotNull(message = ErrorMessage.FIELD_REQUIRED)
        @Min(value = 1, message = "Minimum value of table number should be {value}")
        Integer tableNumber,
        List<@Valid Order> orders) {

}

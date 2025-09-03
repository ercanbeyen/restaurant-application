package com.ercanbeyen.restaurantapplication.dto;

import com.ercanbeyen.restaurantapplication.constant.message.ErrorMessage;
import com.ercanbeyen.restaurantapplication.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BillDto(
         String id,
         @NotNull(message = ErrorMessage.FIELD_REQUIRED)
         Integer tableNumber,
         List<@Valid Order> orders) {

}

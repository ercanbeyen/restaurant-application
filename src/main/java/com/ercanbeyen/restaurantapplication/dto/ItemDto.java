package com.ercanbeyen.restaurantapplication.dto;

import com.ercanbeyen.restaurantapplication.constant.enums.ItemCategory;
import com.ercanbeyen.restaurantapplication.constant.message.ErrorMessage;
import com.ercanbeyen.restaurantapplication.validation.ValidEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemDto(
        Long id,
        @NotBlank(message = ErrorMessage.FIELD_REQUIRED)
        @Size(min = 3, max = 30, message = "Length of name should be between {min} and {max}")
        String name,
        @ValidEnum(enumClass = ItemCategory.class, message = "Invalid item category")
        String category,
        @NotNull(message = ErrorMessage.FIELD_REQUIRED)
        @Min(value = 0, message = "Minimum price should be {value}")
        Double price) {

}

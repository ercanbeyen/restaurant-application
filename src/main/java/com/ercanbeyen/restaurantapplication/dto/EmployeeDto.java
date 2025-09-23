package com.ercanbeyen.restaurantapplication.dto;

import com.ercanbeyen.restaurantapplication.constant.message.ErrorMessage;
import jakarta.validation.constraints.NotBlank;

public record EmployeeDto(
        String id,
        @NotBlank(message = ErrorMessage.FIELD_REQUIRED)
        String fullName) {

}

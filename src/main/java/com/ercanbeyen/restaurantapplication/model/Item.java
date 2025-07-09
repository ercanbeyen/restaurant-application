package com.ercanbeyen.restaurantapplication.model;

import com.ercanbeyen.restaurantapplication.constant.enums.ItemCategory;
import com.ercanbeyen.restaurantapplication.constant.message.ErrorMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = ErrorMessage.FIELD_REQUIRED)
    @Size(min = 3, max = 30, message = "Length of name should be between {min} and {max}")
    private String name;
    @NotNull(message = ErrorMessage.FIELD_REQUIRED)
    @Enumerated(value = EnumType.STRING)
    private ItemCategory category;
    @NotNull(message = ErrorMessage.FIELD_REQUIRED)
    @Min(value = 0, message = "Minimum price should be {value}")
    private Double price;
}

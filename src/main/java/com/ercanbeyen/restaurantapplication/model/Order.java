package com.ercanbeyen.restaurantapplication.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Order {
    private String itemName;
    private Double amount;
}

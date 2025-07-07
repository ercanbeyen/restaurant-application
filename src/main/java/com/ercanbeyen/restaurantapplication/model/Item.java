package com.ercanbeyen.restaurantapplication.model;

import com.ercanbeyen.restaurantapplication.constant.enums.ItemCategory;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private ItemCategory category;
    private Double price;
}

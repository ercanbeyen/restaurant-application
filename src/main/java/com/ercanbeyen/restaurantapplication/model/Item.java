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
    @Column(unique = true, nullable = false, length = 30)
    private String name;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ItemCategory category;
    @Column(nullable = false)
    private Double price;
}

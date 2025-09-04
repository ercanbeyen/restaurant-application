package com.ercanbeyen.restaurantapplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private Integer tableNumber;
    @ElementCollection
    @CollectionTable(
            name = "bill_orders",
            joinColumns = @JoinColumn(
                    name = "table_number",
                    referencedColumnName = "tableNumber"
            )
    )
    private List<Order> orders;
}

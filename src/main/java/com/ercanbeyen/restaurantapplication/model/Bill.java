package com.ercanbeyen.restaurantapplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private Integer tableNumber;
    private LocalDateTime openDate;
    private LocalDateTime updateDate;
    @ManyToOne
    private Employee employee;
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

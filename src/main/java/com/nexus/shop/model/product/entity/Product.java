package com.nexus.shop.model.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import com.nexus.shop.model.product.enums.Category;

@Entity
@Table (name = "product")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    private String description;

    @Column (nullable = false)
    private BigDecimal price;

    @Column (nullable = false)
    private Integer stock;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    public Product(){
    }

    public Product(String name, String description, BigDecimal price, Integer stock, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }
}

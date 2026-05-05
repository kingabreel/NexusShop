package com.nexus.shop.model.analytic.entity;

import com.nexus.shop.model.product.entity.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_analytic")
@Getter
@Setter
public class ProductAnalytic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Product product;

    private Long viewCount = 0L;

    public void incrementViewCount() {
        this.viewCount++;
    }
}

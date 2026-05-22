package com.nexus.shop.model.analytic.entity;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.product.entity.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_analytic")
@Getter
@Setter
public class ProductAnalytic extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    private Long viewCount = 0L;

    public void incrementViewCount() {
        this.viewCount++;
    }
}

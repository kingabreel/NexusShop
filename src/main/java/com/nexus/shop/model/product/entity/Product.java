package com.nexus.shop.model.product.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.enums.Tag;
import com.nexus.shop.model.promotion.entity.Promotion;
import com.nexus.shop.model.store.entity.Store;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Tag> tags = new ArrayList<>();

    private boolean highlight = false;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 384)
    @Column(name = "embedding")
    private float[] embedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private List<Promotion> promotions = new ArrayList<>();
    private Integer soldCount = 0;

    public Product(
            final String name,
            final String description,
            final BigDecimal price,
            final Integer stock,
            final Category category,
            final List<Tag> tags,
            final boolean highlight) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.tags = tags;
        this.highlight = highlight;
    }

    public BigDecimal getDiscountedPrice() {
        LocalDateTime now = LocalDateTime.now();

        Double bestDiscount = this.promotions.stream()
                .filter(p -> now.isAfter(p.getStartDate()) && now.isBefore(p.getEndDate()))
                .map(Promotion::getPercentage)
                .max(Double::compareTo)
                .orElse(0.0);

        BigDecimal discountFactor = BigDecimal.valueOf(1 - (bestDiscount / 100));

        return this.price.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
    }

}

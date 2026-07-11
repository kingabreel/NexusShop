package com.nexus.shop.model.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.enums.Tag;
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
    @Array(length = 1536)
    @Column(name = "embedding")
    private float[] embedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

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

}

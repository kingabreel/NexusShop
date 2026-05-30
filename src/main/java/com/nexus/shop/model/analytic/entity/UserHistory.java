package com.nexus.shop.model.analytic.entity;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_user_product", columnList = "user_id, product_id"),
})
public class UserHistory extends AbstractEntity {

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Enumerated(EnumType.STRING)
    private InteractionType type;

}

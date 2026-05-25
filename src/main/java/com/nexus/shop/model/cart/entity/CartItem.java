package com.nexus.shop.model.cart.entity;

import java.math.BigDecimal;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.product.entity.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
public class CartItem extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal priceAtTime;

    @Column(nullable = false)
    private BigDecimal total;

    public CartItem(final Cart cart, final Product product, final Integer quantity, final BigDecimal priceAtTime) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.priceAtTime = priceAtTime;
        this.total = priceAtTime.multiply(BigDecimal.valueOf(quantity));
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.total = this.priceAtTime.multiply(BigDecimal.valueOf(quantity));
    }

}

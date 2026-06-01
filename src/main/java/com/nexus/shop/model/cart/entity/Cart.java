package com.nexus.shop.model.cart.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.cart.enums.CartStatus;
import com.nexus.shop.model.cart.enums.CouponType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
public class Cart extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public Cart(final User user) {
        this.user = user;
        this.status = CartStatus.ACTIVE;
    }

    public void recalculateTotals() {
        BigDecimal subtotal = this.getItems()
                .stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = BigDecimal.ZERO;

        Coupon coupon = this.getCoupon();

        if (coupon != null) {
            if (coupon.isExpired()) {
                throw new RuntimeException("Coupon expired.");
            }

            if (coupon.isActive()) {
                if (coupon.getType() == CouponType.FIXED) {
                    discount = coupon.getValue();

                } else if (coupon.getType() == CouponType.PERCENTAGE) {
                    discount = subtotal
                            .multiply(coupon.getValue())
                            .divide(BigDecimal.valueOf(100));
                }
            }
        }

        if (discount.compareTo(subtotal) > 0) {
            discount = subtotal;
        }

        this.subtotal = subtotal;
        this.discount = discount;
        this.total = subtotal.subtract(discount);
    }
}

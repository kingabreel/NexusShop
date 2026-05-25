package com.nexus.shop.model.cart.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.cart.enums.CouponType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor
public class Coupon extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    public Coupon(String code, BigDecimal value, CouponType type, boolean active,
            LocalDateTime expirationDate) {
        this.code = code;
        this.value = value;
        this.type = type;
        this.setActive(active);
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }

    public boolean isValid() {
        return isActive() && !isExpired();
    }

}

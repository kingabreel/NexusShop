package com.nexus.shop.api.coupon.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.cart.entity.Coupon;
import com.nexus.shop.model.cart.request.CreateCouponDTO;
import com.nexus.shop.persistence.repository.CouponRepository;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon createCoupon(CreateCouponDTO dto) {
        Coupon coupon = new Coupon(
                dto.code(),
                dto.value(),
                dto.type(),
                true,
                dto.expirationDate());
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found."));
    }

    public void deactivateCoupon(UUID id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found."));
        coupon.setActive(false);
        couponRepository.save(coupon);
    }

    public void deleteCoupon(UUID id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found."));
        couponRepository.delete(coupon);
    }
}

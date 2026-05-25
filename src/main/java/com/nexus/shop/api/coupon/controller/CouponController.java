package com.nexus.shop.api.coupon.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.coupon.service.CouponService;
import com.nexus.shop.model.ApiResponse;
import com.nexus.shop.model.cart.entity.Coupon;
import com.nexus.shop.model.cart.request.CreateCouponDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Coupon>> createCoupon(
            @RequestBody @Valid CreateCouponDTO dto) {
        try {
            Coupon coupon = couponService.createCoupon(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(coupon, "Coupon created"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Coupon>>> getAllCoupons() {
        try {
            List<Coupon> coupons = couponService.getAllCoupons();
            return ResponseEntity.ok(new ApiResponse<>(coupons, "Coupons retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<Coupon>> getCouponByCode(@PathVariable String code) {
        try {
            Coupon coupon = couponService.getCouponByCode(code);
            return ResponseEntity.ok(new ApiResponse<>(coupon, "Coupon retrieved"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateCoupon(@PathVariable UUID id) {
        try {
            couponService.deactivateCoupon(id);
            return ResponseEntity.ok(new ApiResponse<>(null, "Coupon deactivated"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable UUID id) {
        try {
            couponService.deleteCoupon(id);
            return ResponseEntity.ok(new ApiResponse<>(null, "Coupon deleted"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }
}

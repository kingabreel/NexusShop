package com.nexus.shop.persistence.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductSpecification {

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, cb) -> minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, cb) -> maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> categoryEquals(Category category) {
        return (root, query, cb) -> category == null ? null : cb.equal(root.get("category"), category);
    }
}

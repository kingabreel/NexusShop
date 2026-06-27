package com.nexus.shop.model.rating.entity;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Ratings")
@NoArgsConstructor
@Getter
@Setter
public class Rating extends AbstractEntity {
    @Column(nullable = false)
    Double rating;

    String comment;

    boolean anonymous = false;

    @ManyToOne
    User user;

    @ManyToOne
    Product product;

    public Rating(Double rating, String comment, boolean anonymous, User user, Product product) {
        this.rating = rating;
        this.comment = comment;
        this.anonymous = anonymous;
        this.user = user;
        this.product = product;
    }

}

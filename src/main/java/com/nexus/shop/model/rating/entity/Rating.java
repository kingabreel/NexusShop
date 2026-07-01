package com.nexus.shop.model.rating.entity;

import com.nexus.shop.model.AbstractEntity;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.rating.validation.ValidRating;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ratings")
@NoArgsConstructor
@Getter
@Setter
public class Rating extends AbstractEntity {
    @Column(nullable = false)
    @ValidRating
    private Double rating;

    private String comment;

    private boolean anonymous = false;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private String imageUrl;

    public Rating(Double rating, String comment, boolean anonymous, User user, Product product, String imageUrl) {
        this.rating = rating;
        this.comment = comment;
        this.anonymous = anonymous;
        this.user = user;
        this.product = product;
        this.imageUrl = imageUrl;
    }

}

package com.nexus.shop.api.rating.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.rating.dto.RatingUpdatePartialDTO;
import com.nexus.shop.model.rating.entity.Rating;
import com.nexus.shop.model.rating.request.RatingCreateDTO;
import com.nexus.shop.model.rating.response.RatingResponseDTO;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.persistence.repository.RatingRepository;
import com.nexus.shop.utils.converters.ConverterUtil;
import com.nexus.shop.utils.helpers.AuthenticatedUserHelper;

@Service
public class RatingService {
    ProductRepository productRepository;
    RatingRepository ratingRepository;
    AuthenticatedUserHelper authenticatedUserHelper;

    public RatingService(ProductRepository productRepository, RatingRepository ratingRepository,
            AuthenticatedUserHelper authenticatedUserHelper) {
        this.productRepository = productRepository;
        this.ratingRepository = ratingRepository;
        this.authenticatedUserHelper = authenticatedUserHelper;
    }

    public RatingResponseDTO create(RatingCreateDTO dto) {
        User user = authenticatedUserHelper.getAuthenticatedUser();

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found."));

        Rating rating = new Rating(
                dto.rating(),
                dto.comment(),
                dto.anonymous(),
                user,
                product);

        Rating saved = ratingRepository.save(rating);

        return ConverterUtil.toDTO(saved);
    }

    public List<RatingResponseDTO> findByProduct(final UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));

        List<Rating> ratings = ratingRepository.findByProduct(product);

        return ratings.stream()
                .map(ConverterUtil::toDTO)
                .toList();
    }

    public RatingResponseDTO updatePartial(final UUID ratingId, final RatingUpdatePartialDTO dto) {
        User currentUser = authenticatedUserHelper.getAuthenticatedUser();

        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found."));

        if (!rating.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only edit your own reviews");
        }

        if (dto.rating() != null) {
            rating.setRating(dto.rating());
        }
        if (dto.comment() != null) {
            rating.setComment(dto.comment());
        }
        if (dto.anonymous() != null) {
            rating.setAnonymous(dto.anonymous());
        }

        Rating saved = ratingRepository.save(rating);

        return ConverterUtil.toDTO(saved);
    }

    public void delete(final UUID id) {
        User currentUser = authenticatedUserHelper.getAuthenticatedUser();

        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found."));

        if (!rating.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        ratingRepository.delete(rating);
    }

}

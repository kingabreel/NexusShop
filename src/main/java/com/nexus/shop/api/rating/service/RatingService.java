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
import com.nexus.shop.utils.helpers.ImageUploadHelper;

@Service
public class RatingService {
    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;
    private final AuthenticatedUserHelper authenticatedUserHelper;
    private final ImageUploadHelper imageUploadHelper;

    public RatingService(ProductRepository productRepository, RatingRepository ratingRepository,
                        AuthenticatedUserHelper authenticatedUserHelper, ImageUploadHelper imageUploadHelper) {
        this.productRepository = productRepository;
        this.ratingRepository = ratingRepository;
        this.authenticatedUserHelper = authenticatedUserHelper;
        this.imageUploadHelper = imageUploadHelper;
    }

    public RatingResponseDTO create(RatingCreateDTO dto) {
        User user = authenticatedUserHelper.getAuthenticatedUser();

        boolean alreadyRated = ratingRepository.existsByProduct_IdAndUser_Id(dto.productId(), user.getId());

        if (alreadyRated) {
            throw new IllegalArgumentException("User already rated this product.");
        }

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found."));

        String imageUrl = null;

        if (dto.imageBase64() != null && !dto.imageBase64().isBlank()) {
            imageUrl = imageUploadHelper.saveImage(dto.imageBase64());
        }

        Rating rating = new Rating(
                dto.rating(),
                dto.comment(),
                dto.anonymous(),
                user,
                product,
                imageUrl);

        Rating saved = ratingRepository.save(rating);

        return ConverterUtil.toDTO(saved);
    }

    public List<RatingResponseDTO> findByProduct(final UUID productId) {
        List<Rating> ratings = ratingRepository.findByProduct_Id(productId);

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

        if (dto.imageBase64() != null
                && !dto.imageBase64().isBlank()
                && imageUploadHelper.isValidBase64Image(dto.imageBase64())) {
            String imageUrl = imageUploadHelper.saveImage(dto.imageBase64());
            rating.setImageUrl(imageUrl);
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

    public Double getAverageRating(UUID productId) {
        Double average = ratingRepository.findAverageRatingByProductId(productId);

        return average == null ? 0.0 : average;
    }

    public Long getRatingCount(UUID productId) {
        return ratingRepository.countByProduct_Id(productId);
    }
}


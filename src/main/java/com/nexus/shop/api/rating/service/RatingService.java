package com.nexus.shop.api.rating.service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.rating.dto.RatingUpdatePartialDTO;
import com.nexus.shop.model.rating.entity.Rating;
import com.nexus.shop.model.rating.request.RatingCreateDTO;
import com.nexus.shop.model.rating.response.RatingResponseDTO;
import com.nexus.shop.persistence.cdn.CdnInterface;
import com.nexus.shop.persistence.cdn.LocalImpl;
import com.nexus.shop.persistence.cdn.MinioImpl;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.persistence.repository.RatingRepository;
import com.nexus.shop.utils.converters.ConverterUtil;
import com.nexus.shop.utils.helpers.AuthenticatedUserHelper;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;

@Service
public class RatingService {
    private ProductRepository productRepository;
    private RatingRepository ratingRepository;
    private AuthenticatedUserHelper authenticatedUserHelper;

    private CdnInterface cdnService;

    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url}")
    private String url;

    @Value("${local.cdn.activate:true}")
    private boolean localCdnActivate;

    @Value("${local.cdn.path:./cdn}")
    private String localCdnPath;

    public RatingService(ProductRepository productRepository, RatingRepository ratingRepository,
            AuthenticatedUserHelper authenticatedUserHelper, MinioClient minioClient) {
        this.productRepository = productRepository;
        this.ratingRepository = ratingRepository;
        this.authenticatedUserHelper = authenticatedUserHelper;
        this.minioClient = minioClient;

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

        if (!StringUtils.isBlank(dto.imageBase64()) && isValidBase64Image(dto.imageBase64())) {
            imageUrl = saveImage(null, dto.imageBase64());
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

        if (!StringUtils.isBlank(dto.imageBase64()) && isValidBase64Image(dto.imageBase64())) {
            String imageUrl = saveImage(null, dto.imageBase64());
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

    // Esses proximos 3 metodos futuramente devem ir para um helper porque os
    // produtos também tem imagens, os users tem foto de perfil...
    // TODO: trocar metodo para private
    private String saveImage(String filename, String base64Content) {

        if (StringUtils.isBlank(filename)) {
            filename = UUID.randomUUID().toString() + ".jpg";
        }

        byte[] fileContent = decodeBase64(base64Content);

        return cdnService.uploadFile(filename, fileContent);
    }

    @PostConstruct
    private void initCdn() {
        // esses CDNs estão salvando tudo na mesma pasta, futuramente precisamos separar
        // pasta de produtos, usuarios + criar sub-pastas para subtipos
        if (this.localCdnActivate) {
            this.cdnService = new LocalImpl(this.localCdnPath);
        } else {
            this.cdnService = new MinioImpl(minioClient, this.bucketName, this.url);
        }
    }

    private byte[] decodeBase64(String base64Content) {
        if (base64Content.contains(",")) {
            base64Content = base64Content.split(",")[1];
        }

        return Base64.getDecoder().decode(base64Content);
    }

    private boolean isValidBase64Image(String base64) {
        try {
            String content = base64.contains(",") ? base64.split(",")[1] : base64;

            if (content.length() % 4 != 0)
                return false;

            byte[] decoded = Base64.getDecoder().decode(content);

            if (decoded.length < 4)
                return false;

            boolean isPng = decoded[0] == (byte) 0x89 && decoded[1] == 0x50;
            boolean isJpg = decoded[0] == (byte) 0xFF && decoded[1] == (byte) 0xD8;

            return isPng || isJpg;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Double getAverageRating(UUID productId) {
        Double average = ratingRepository.findAverageRatingByProductId(productId);

        return average == null ? 0.0 : average;
    }

    public Long getRatingCount(UUID productId) {
        return ratingRepository.countByProduct_Id(productId);
    }
}

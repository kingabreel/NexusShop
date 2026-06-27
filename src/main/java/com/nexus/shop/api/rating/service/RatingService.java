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

@Service
public class RatingService {
    private ProductRepository productRepository;
    private RatingRepository ratingRepository;
    private AuthenticatedUserHelper authenticatedUserHelper;

    private CdnInterface cdnService;

    @Value("${local.cdn.activate:true}")
    private boolean localCdnActivate;
    @Value("${local.cdn.path:./cdn}")
    private String localCdnPath;

    public RatingService(ProductRepository productRepository, RatingRepository ratingRepository,
            AuthenticatedUserHelper authenticatedUserHelper) {
        this.productRepository = productRepository;
        this.ratingRepository = ratingRepository;
        this.authenticatedUserHelper = authenticatedUserHelper;

        if (this.cdnService == null) {
            this.initCdn();
        }
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

    // Esses proximos 3 metodos futuramente devem ir para um helper porque os
    // produtos também tem imagens, os users tem foto de perfil...
    // TODO: trocar metodo para private
    public String saveImage(String filename, String base64Content) {
        if (this.cdnService == null) {
            initCdn();
        }

        if (StringUtils.isBlank(filename)) {
            filename = UUID.randomUUID().toString() + ".jpg";
        }

        byte[] fileContent = decodeBase64(base64Content);

        return this.cdnService.uploadFile(filename, fileContent);
    }

    private void initCdn() {
        // esses CDNs estão salvando tudo na mesma pasta, futuramente precisamos separar
        // pasta de produtos, usuarios + criar sub-pastas para subtipos
        if (this.localCdnActivate) {
            this.cdnService = new LocalImpl(this.localCdnPath);
        } else {
            this.cdnService = new MinioImpl();
        }
    }

    // TODO: Remover ou trocar para private
    // Imagem já deve ser retornada no objeto response para requests de ratings :)
    public byte[] downloadFile(String filename) {
        return this.cdnService.downloadFile(filename);
    }

    private byte[] decodeBase64(String base64Content) {
        if (base64Content.contains(",")) {
            base64Content = base64Content.split(",")[1];
        }

        return Base64.getDecoder().decode(base64Content);
    }
}

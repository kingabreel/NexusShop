package com.nexus.shop.api.promotion.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus.shop.api.rating.service.RatingService;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.model.promotion.dto.PromotionPatchDTO;
import com.nexus.shop.model.promotion.entity.Promotion;
import com.nexus.shop.model.promotion.request.PromotionRequestDTO;
import com.nexus.shop.model.promotion.response.PromotionResponseDTO;
import com.nexus.shop.model.store.entity.Store;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.persistence.repository.PromotionRepository;
import com.nexus.shop.utils.converters.ConverterUtil;
import com.nexus.shop.utils.helpers.AuthenticatedUserHelper;

@Service
public class PromotionService {

    final PromotionRepository promotionRepository;
    final ProductRepository productRepository;
    final AuthenticatedUserHelper authenticatedUserHelper;
    final RatingService ratingService;

    public PromotionService(PromotionRepository promotionRepository, ProductRepository productRepository,
            AuthenticatedUserHelper authenticatedUserHelper, RatingService ratingService) {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.authenticatedUserHelper = authenticatedUserHelper;
        this.ratingService = ratingService;
    }

    @Transactional
    public PromotionResponseDTO create(final PromotionRequestDTO dto) {
        Store store = getCurrentUserStore();

        Promotion promotion = new Promotion();

        promotion.setName(dto.name());
        promotion.setStartDate(dto.startDate());
        promotion.setEndDate(dto.endDate());
        promotion.setPercentage(dto.percentage());
        promotion.setStore(store);

        if (dto.productsId() != null) {
            List<Product> products = findAndValidateProducts(dto.productsId(), store);
            promotion.setProducts(products);
        }

        Promotion saved = promotionRepository.save(promotion);

        return ConverterUtil.toDTO(saved, toProductDTOs(saved));
    }

    @Transactional(readOnly = true)
    public List<PromotionResponseDTO> getAll() {
        List<Promotion> promotions = promotionRepository.findAll();

        return promotions.stream()
                .map(p -> ConverterUtil.toDTO(p, toProductDTOs(p)))
                .toList();
    }

    @Transactional(readOnly = true)
    public PromotionResponseDTO getById(final UUID id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        return ConverterUtil.toDTO(promotion, toProductDTOs(promotion));
    }

    @Transactional(readOnly = true)
    public List<PromotionResponseDTO> getActive() {
        List<Promotion> promotions = promotionRepository.findAll();

        return promotions.stream()
                .filter(p -> LocalDateTime.now().isAfter(p.getStartDate())
                        && LocalDateTime.now().isBefore(p.getEndDate()))
                .map(p -> ConverterUtil.toDTO(p, toProductDTOs(p)))
                .toList();
    }

    @Transactional
    public PromotionResponseDTO update(final UUID id, final PromotionPatchDTO dto) {
        Store store = getCurrentUserStore();
        Promotion promotion = findPromotionOrThrow(id);
        assertUserOwnsPromotion(promotion, store);

        if (dto.productsId() != null) {
            List<Product> products = findAndValidateProducts(dto.productsId(), store);
            promotion.setProducts(products);
        }

        if (dto.name() != null) {
            promotion.setName(dto.name());
        }

        if (dto.startDate() != null) {
            promotion.setStartDate(dto.startDate());
        }

        if (dto.endDate() != null) {
            promotion.setEndDate(dto.endDate());
        }

        if (dto.percentage() != null) {
            promotion.setPercentage(dto.percentage());
        }

        final Promotion update = promotionRepository.save(promotion);

        return ConverterUtil.toDTO(update, toProductDTOs(update));
    }

    @Transactional
    public void delete(final UUID id) {
        Store store = getCurrentUserStore();
        Promotion promotion = findPromotionOrThrow(id);
        assertUserOwnsPromotion(promotion, store);
        promotionRepository.delete(promotion);
    }

    @Transactional
    public PromotionResponseDTO addPromotionProduct(final UUID id, final UUID productId) {
        Store store = getCurrentUserStore();
        Promotion promotion = findPromotionOrThrow(id);
        assertUserOwnsPromotion(promotion, store);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStore() == null || !product.getStore().getId().equals(store.getId())) {
            throw new RuntimeException("Product does not belong to your store.");
        }

        if (promotion.getProducts().contains(product)) {
            throw new RuntimeException("Product is already part of this promotion.");
        }

        promotion.getProducts().add(product);

        promotionRepository.save(promotion);

        return ConverterUtil.toDTO(promotion, toProductDTOs(promotion));

    }

    @Transactional
    public void deletePromotionProduct(final UUID productId, final UUID id) {
        Store store = getCurrentUserStore();
        Promotion promotion = findPromotionOrThrow(id);
        assertUserOwnsPromotion(promotion, store);

        boolean removed = promotion.getProducts().removeIf(p -> p.getId().equals(productId));

        if (!removed) {
            throw new RuntimeException("Product is not part of this promotion");
        }

        promotionRepository.save(promotion);

    }

    private List<ProductResponseDTO> toProductDTOs(final Promotion promotion) {
        return promotion.getProducts().stream()
                .map(product -> ConverterUtil.toDTO(
                        product,
                        ratingService.getAverageRating(product.getId()),
                        ratingService.getRatingCount(product.getId())))
                .toList();
    }

    private Store getCurrentUserStore() {
        final User user = authenticatedUserHelper.getAuthenticatedUser();

        if (user.getStore() == null) {
            throw new RuntimeException("User does not have a store associated.");
        }

        return user.getStore();
    }

    private List<Product> findAndValidateProducts(final List<UUID> productsId, final Store store) {
        List<Product> products = productRepository.findAllById(productsId);

        if (products.size() != productsId.size()) {
            throw new RuntimeException("One or more products were not found.");
        }

        for (Product product : products) {
            if (product.getStore() == null || !product.getStore().getId().equals(store.getId())) {
                throw new RuntimeException("Product does not belong to your store.");
            }
        }

        return products;
    }

    private Promotion findPromotionOrThrow(final UUID id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    private void assertUserOwnsPromotion(final Promotion promotion, final Store store) {
        if (!promotion.getStore().getId().equals(store.getId())) {
            throw new RuntimeException("You are not allowed to manage this promotion.");
        }
    }

}

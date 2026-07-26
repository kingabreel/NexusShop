package com.nexus.shop.api.chatbot.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nexus.shop.api.embeddings.OnnxEmbeddingService;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.chatbot.request.ChatbotRequestDto;
import com.nexus.shop.model.chatbot.response.ChatbotProductRecommendationResponseDTO;
import com.nexus.shop.model.chatbot.response.ChatbotProductResponseDTO;
import com.nexus.shop.model.chatbot.response.IChatbotResponseDTO;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.enums.Tag;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.persistence.repository.UserHistoryRepository;
import com.nexus.shop.utils.helpers.AuthenticatedUserHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotService {

        private final ProductRepository productRepository;
        private final OnnxEmbeddingService embeddingService;
        private final AuthenticatedUserHelper authenticatedUserHelper;
        private final UserHistoryRepository userHistoryRepository;
        
        public IChatbotResponseDTO process(ChatbotRequestDto request) {

                return switch (request.option()) {
                        case FIND_PRODUCT -> new ChatbotProductRecommendationResponseDTO(
                                        this.subOptionsForFindProduct(request, request.messageText()));
                        case RECOMMENDATIONS -> new ChatbotProductRecommendationResponseDTO(
                                        this.subOptionsForRecommendations(request, request.messageText()));
                        default -> throw new UnsupportedOperationException(
                                        "Option not implemented: " + request.option());
                };
        }

        private List<ChatbotProductResponseDTO> subOptionsForFindProduct(final ChatbotRequestDto request,
                        final String message) {
                List<ChatbotProductResponseDTO> products = new ArrayList<>();

                switch (request.subOption()) {

                        case SEARCH_BY_NAME ->
                                products = this.searchByName(message);

                        case DESCRIBE_NEEDS ->
                                products = this.recommendUsingDescription(message);

                        case SEARCH_BY_CATEGORY ->
                                products = this.searchByCategory(message);

                        case SEARCH_BY_PRICE_RANGE ->
                                products = this.searchByPrice(message);

                        default -> List.of();
                }

                return products;
        }

        private List<ChatbotProductResponseDTO> subOptionsForRecommendations(final ChatbotRequestDto request,
                        final String message) {
                List<ChatbotProductResponseDTO> products = new ArrayList<>();

                switch (request.subOption()) {

                        case VIEW_RECOMMENDATIONS ->
                                products = this.getProductsRecommendationForUser();

                        case VIEW_PROMOTIONS ->
                                products = this.searchByName(message);

                        case BEST_VALUE ->
                                products = this.searchByPrice(message);

                        case BEST_SELLERS ->
                                products = this.topRated();

                        case TOP_RATED ->
                                products = this.topRated();

                        case NEW_ARRIVALS ->
                                products = this.newArrivals();

                        default -> List.of();
                }

                return products;
        }

        private List<ChatbotProductResponseDTO> searchByName(String name) {

                return productRepository.findByNameContainingIgnoreCase(name)
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> searchByCategory(String category) {

                Category cat;

                try {
                        cat = Category.valueOf(category.trim().toUpperCase());
                } catch (IllegalArgumentException ex) {
                        return List.of();
                }

                return productRepository.findByCategory(cat)
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> searchByPrice(final String text) {

                try {

                        final String[] values = text.split("-");

                        final BigDecimal min = new BigDecimal(values[0].trim());
                        final BigDecimal max = new BigDecimal(values[1].trim());

                        return productRepository.findByPriceBetween(min, max)
                                        .stream()
                                        .map(this::toDto)
                                        .toList();

                } catch (Exception e) {
                        return List.of();
                }
        }

        private List<ChatbotProductResponseDTO> recommendUsingDescription(String description) {

                final float[] embeddings = this.embeddingService.generate(description).getVector();

                return productRepository.searchSimilar(
                                embeddings,
                                10)
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        // private List<ChatbotProductResponseDTO> bestSellers() {

        // return productRepository.findTop10ByOrderBySoldCountDesc()
        // .stream()
        // .map(this::toDto)
        // .toList();
        // }

        private List<ChatbotProductResponseDTO> topRated() {

                return productRepository.findTopRated(PageRequest.of(0, 10))
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> newArrivals() {

                return productRepository.findTop10ByOrderByCreatedAtDesc()
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> getProductsRecommendationForUser() {

                User user = authenticatedUserHelper.getAuthenticatedUser();

                List<Product> history = this.userHistoryRepository.findTop20HistoryProducts(
                                user,
                                PageRequest.of(0, 20));

                if (history.isEmpty()) {
                        return productRepository.findTop10ByOrderByCreatedAtDesc()
                                        .stream()
                                        .map(this::toDto)
                                        .toList();
                }

                List<Category> categories = history.stream()
                                .map(Product::getCategory)
                                .distinct()
                                .toList();

                List<Tag> tags = history.stream()
                                .flatMap(p -> p.getTags().stream())
                                .distinct()
                                .toList();

                List<UUID> excludedIds = history.stream()
                                .map(Product::getId)
                                .distinct()
                                .toList();

                return productRepository.findRecommendations(
                                categories,
                                tags,
                                excludedIds,
                                PageRequest.of(0, 10))
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private ChatbotProductResponseDTO toDto(Product product) {

                // TODO: fix
                return new ChatbotProductResponseDTO(
                                UUID.randomUUID(),
                                product.getName(),
                                product.getDescription(),
                                product.getPrice(),
                                product.getStock(),
                                product.getCategory(),
                                product.getTags(),
                                product.isHighlight());
        }

}

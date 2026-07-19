package com.nexus.shop.api.chatbot.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nexus.shop.infra.external.CohereApiCall;
import com.nexus.shop.model.chatbot.request.ChatbotRequestDto;
import com.nexus.shop.model.chatbot.response.ChatbotProductRecommendationResponseDTO;
import com.nexus.shop.model.chatbot.response.ChatbotProductResponseDTO;
import com.nexus.shop.model.chatbot.response.IChatbotResponseDTO;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.persistence.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ProductRepository productRepository;
    private final CohereApiCall cohereApiCall;

    public IChatbotResponseDTO process(ChatbotRequestDto request) {

        return switch (request.subOption()) {

            case SEARCH_BY_NAME ->
                    new ChatbotProductRecommendationResponseDTO(
                            searchByName(request.messageText()));

            case SEARCH_BY_CATEGORY ->
                    new ChatbotProductRecommendationResponseDTO(
                            searchByCategory(request.messageText()));

            case SEARCH_BY_PRICE_RANGE ->
                    new ChatbotProductRecommendationResponseDTO(
                            searchByPrice(request.messageText()));

            case DESCRIBE_NEEDS ->
                    new ChatbotProductRecommendationResponseDTO(
                            recommendUsingDescription(request.messageText()));

            // case BEST_SELLERS ->
            //         new ChatbotProductRecommendationResponseDTO(
            //                 bestSellers());

            case TOP_RATED ->
                    new ChatbotProductRecommendationResponseDTO(
                            topRated());

            case NEW_ARRIVALS ->
                    new ChatbotProductRecommendationResponseDTO(
                            newArrivals());

            default ->
                    throw new UnsupportedOperationException(
                            "SubOption not implemented: " + request.subOption());
        };
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

    private List<ChatbotProductResponseDTO> searchByPrice(String text) {

        try {

            String[] values = text.split("-");

            BigDecimal min = new BigDecimal(values[0].trim());
            BigDecimal max = new BigDecimal(values[1].trim());

            return productRepository.findByPriceBetween(min, max)
                    .stream()
                    .map(this::toDto)
                    .toList();

        } catch (Exception e) {
            return List.of();
        }
    }

    private List<ChatbotProductResponseDTO> recommendUsingDescription(String description) {

        List<float[]> embeddings = cohereApiCall.generateEmbeddings(
                List.of(description));

        if (embeddings.isEmpty()) {
            return List.of();
        }

        return productRepository.searchSimilar(
                        embeddings.get(0),
                        10)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // private List<ChatbotProductResponseDTO> bestSellers() {

    //     return productRepository.findTop10ByOrderBySoldCountDesc()
    //             .stream()
    //             .map(this::toDto)
    //             .toList();
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
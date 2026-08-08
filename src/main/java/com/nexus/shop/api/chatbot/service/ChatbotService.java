package com.nexus.shop.api.chatbot.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nexus.shop.api.embeddings.OnnxEmbeddingService;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.chatbot.enums.ChatbotOptions;
import com.nexus.shop.model.chatbot.enums.ChatbotSubOptions;
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

        public IChatbotResponseDTO process(final ChatbotRequestDto request) {

                return switch (ChatbotOptions.valueOf(request.option().id())) {
                        case FIND_PRODUCT -> new ChatbotProductRecommendationResponseDTO(
                                        this.subOptionsForFindProduct(request, request.messageText()));
                        case RECOMMENDATIONS -> new ChatbotProductRecommendationResponseDTO(
                                        this.subOptionsForRecommendations(request, request.messageText()));
                        case COMPARE_PRODUCTS -> new ChatbotProductRecommendationResponseDTO(
                                        this.subOptionsForCompareProducts(request, request.messageText()));
                        case PRODUCT_INFORMATION -> new ChatbotProductRecommendationResponseDTO(
                                        this.subOptionsForProductInformation(request, request.messageText()));
                        case ORDER_SUPPORT -> new ChatbotProductRecommendationResponseDTO(
                                        this.subOptionsForOrderSupport(request));
                        case CUSTOMER_SUPPORT -> new ChatbotProductRecommendationResponseDTO(
                                        this.subOptionsForCustomerSupport(request));
                        default -> throw new UnsupportedOperationException(
                                        "Option not implemented: " + request.option());
                };
        }

        private List<ChatbotProductResponseDTO> subOptionsForFindProduct(final ChatbotRequestDto request,
                        final String message) {
                List<ChatbotProductResponseDTO> products;

                switch (ChatbotSubOptions.valueOf(request.subOption().id())) {

                        case SEARCH_BY_NAME ->
                                products = this.searchByEmbedding(message);

                        case DESCRIBE_NEEDS ->
                                products = this.recommendUsingDescription(message);

                        case SEARCH_BY_CATEGORY ->
                                products = this.searchByCategory(message);

                        case SEARCH_BY_PRICE_RANGE ->
                                products = this.searchByPrice(message);

                        default -> products = List.of();
                }

                return products;
        }

        private List<ChatbotProductResponseDTO> subOptionsForRecommendations(final ChatbotRequestDto request,
                        final String message) {
                List<ChatbotProductResponseDTO> products;

                switch (ChatbotSubOptions.valueOf(request.subOption().id())) {

                        case VIEW_RECOMMENDATIONS ->
                                products = this.getProductsRecommendationForUser();

                        case VIEW_PROMOTIONS ->
                                products = this.searchByEmbedding(message);

                        case BEST_VALUE ->
                                products = this.searchByPrice(message);

                        case BEST_SELLERS ->
                                products = this.bestSellers();

                        case TOP_RATED ->
                                products = this.topRated();

                        case NEW_ARRIVALS ->
                                products = this.newArrivals();

                        case SIMILAR_PRODUCTS ->
                                products = this.findSimilarProducts(message);

                        default -> products = List.of();
                }

                return products;
        }

        private List<ChatbotProductResponseDTO> subOptionsForCompareProducts(final ChatbotRequestDto request,
                        final String message) {
                List<ChatbotProductResponseDTO> products;

                switch (ChatbotSubOptions.valueOf(request.subOption().id())) {

                        case COMPARE_TWO_PRODUCTS ->
                                products = this.compareTwoProducts(message);

                        case WHICH_IS_BETTER ->
                                products = this.whichIsBetter();

                        case VIEW_DIFFERENCES ->
                                products = this.viewDifferences(message);

                        default -> products = List.of();
                }

                return products;
        }

        private List<ChatbotProductResponseDTO> subOptionsForProductInformation(final ChatbotRequestDto request,
                        final String message) {
                List<ChatbotProductResponseDTO> products;

                switch (ChatbotSubOptions.valueOf(request.subOption().id())) {

                        case VIEW_FEATURES ->
                                products = this.searchByEmbedding(message);

                        case VIEW_SPECIFICATIONS ->
                                products = this.searchByEmbedding(message);

                        case CHECK_AVAILABILITY ->
                                products = this.checkAvailability(message);

                        case VIEW_WARRANTY ->
                                products = this.viewWarranty();

                        case PAYMENT_OPTIONS ->
                                products = this.paymentOptions();

                        case RELATED_PRODUCTS ->
                                products = this.relatedProducts(message);

                        default -> products = List.of();
                }

                return products;
        }

        private List<ChatbotProductResponseDTO> subOptionsForOrderSupport(final ChatbotRequestDto request) {
                List<ChatbotProductResponseDTO> products;

                switch (ChatbotSubOptions.valueOf(request.subOption().id())) {

                        case TRACK_ORDER ->
                                products = this.trackOrder();

                        case SHIPPING_STATUS ->
                                products = this.shippingStatus();

                        case ESTIMATED_DELIVERY ->
                                products = this.estimatedDelivery();

                        case RETURNS_AND_REFUNDS ->
                                products = this.returnsAndRefunds();

                        default -> products = List.of();
                }

                return products;
        }

        private List<ChatbotProductResponseDTO> subOptionsForCustomerSupport(final ChatbotRequestDto request) {
                List<ChatbotProductResponseDTO> products;

                switch (ChatbotSubOptions.valueOf(request.subOption().id())) {

                        case PAYMENT_ISSUES ->
                                products = this.paymentIssues();

                        case CANCELLATIONS ->
                                products = this.cancellations();

                        case CONTACT_AGENT ->
                                products = this.contactAgent();

                        default -> products = List.of();
                }

                return products;
        }

        private List<ChatbotProductResponseDTO> searchByEmbedding(final String text) {

                final float[] embeddings = this.embeddingService.generate(text).getVector();

                return this.productRepository.searchSimilar(
                                embeddings,
                                10)
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> searchByCategory(final String category) {

                final Category cat;

                try {
                        cat = Category.valueOf(category.trim().toUpperCase());
                } catch (final IllegalArgumentException ex) {
                        return List.of();
                }

                return this.productRepository.findByCategory(cat)
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> searchByPrice(final String text) {

                try {

                        final String[] values = text.split("-");

                        final BigDecimal min = new BigDecimal(values[0].trim());
                        final BigDecimal max = new BigDecimal(values[1].trim());

                        return this.productRepository.findByPriceBetween(min, max)
                                        .stream()
                                        .map(this::toDto)
                                        .toList();

                } catch (final Exception e) {
                        return List.of();
                }
        }

        private List<ChatbotProductResponseDTO> recommendUsingDescription(final String description) {

                final float[] embeddings = this.embeddingService.generate(description).getVector();

                return this.productRepository.searchSimilar(
                                embeddings,
                                10)
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> bestSellers() {

                return this.productRepository.findTop10ByOrderBySoldCountDesc()
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> topRated() {

                return this.productRepository.findTopRated(PageRequest.of(0, 10))
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> newArrivals() {

                return this.productRepository.findTop10ByOrderByCreatedAtDesc()
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> findSimilarProducts(final String text) {

                return this.searchByEmbedding(text);
        }

        private List<ChatbotProductResponseDTO> compareTwoProducts(final String text) {

                return this.searchByEmbedding(text);
        }

        private List<ChatbotProductResponseDTO> whichIsBetter() {

                return this.topRated();
        }

        private List<ChatbotProductResponseDTO> viewDifferences(final String text) {

                return this.searchByEmbedding(text);
        }

        private List<ChatbotProductResponseDTO> checkAvailability(final String text) {

                try {
                        final String[] values = text.split("-");
                        final BigDecimal min = new BigDecimal(values[0].trim());
                        final BigDecimal max = new BigDecimal(values[1].trim());

                        return this.productRepository.findByPriceBetween(min, max)
                                        .stream()
                                        .filter(p -> p.getStock() > 0)
                                        .map(this::toDto)
                                        .toList();

                } catch (final Exception e) {
                        return this.productRepository.findByStockGreaterThan(0)
                                        .stream()
                                        .map(this::toDto)
                                        .toList();
                }
        }

        private List<ChatbotProductResponseDTO> viewWarranty() {

                return this.productRepository.findByHighlightTrue()
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> paymentOptions() {

                return this.productRepository.findByHighlightTrue()
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> relatedProducts(final String text) {

                final float[] embeddings = this.embeddingService.generate(text).getVector();

                final List<Product> similarProducts = this.productRepository.searchSimilar(
                                embeddings,
                                5);

                if (similarProducts.isEmpty()) {
                        return List.of();
                }

                final Product firstProduct = similarProducts.get(0);
                final Category category = firstProduct.getCategory();
                final UUID excludedId = firstProduct.getId();

                return this.productRepository.findTop10ByCategoryAndIdNot(
                                category,
                                excludedId)
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private List<ChatbotProductResponseDTO> trackOrder() {

                return List.of();
        }

        private List<ChatbotProductResponseDTO> shippingStatus() {

                return List.of();
        }

        private List<ChatbotProductResponseDTO> estimatedDelivery() {

                return List.of();
        }

        private List<ChatbotProductResponseDTO> returnsAndRefunds() {

                return List.of();
        }

        private List<ChatbotProductResponseDTO> paymentIssues() {

                return List.of();
        }

        private List<ChatbotProductResponseDTO> cancellations() {

                return List.of();
        }

        private List<ChatbotProductResponseDTO> contactAgent() {

                return List.of();
        }

        private List<ChatbotProductResponseDTO> getProductsRecommendationForUser() {

                final User user = this.authenticatedUserHelper.getAuthenticatedUser();

                final List<Product> history = this.userHistoryRepository.findTop20HistoryProducts(
                                user,
                                PageRequest.of(0, 20));

                if (history.isEmpty()) {
                        return this.productRepository.findTop10ByOrderByCreatedAtDesc()
                                        .stream()
                                        .map(this::toDto)
                                        .toList();
                }

                final List<Category> categories = history.stream()
                                .map(Product::getCategory)
                                .distinct()
                                .toList();

                final List<Tag> tags = history.stream()
                                .flatMap(p -> p.getTags().stream())
                                .distinct()
                                .toList();

                final List<UUID> excludedIds = history.stream()
                                .map(Product::getId)
                                .distinct()
                                .toList();

                return this.productRepository.findRecommendations(
                                categories,
                                tags,
                                excludedIds,
                                PageRequest.of(0, 10))
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private ChatbotProductResponseDTO toDto(final Product product) {

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

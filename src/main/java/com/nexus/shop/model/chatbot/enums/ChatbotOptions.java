package com.nexus.shop.model.chatbot.enums;

import java.util.List;

public enum ChatbotOptions {

        FIND_PRODUCT(
                        "Find Product",
                        List.of(
                                        ChatbotSubOptions.SEARCH_BY_NAME,
                                        ChatbotSubOptions.DESCRIBE_NEEDS,
                                        ChatbotSubOptions.SEARCH_BY_CATEGORY,
                                        ChatbotSubOptions.SEARCH_BY_PRICE_RANGE)),

        RECOMMENDATIONS(
                        "Recommendations",
                        List.of(
                                        ChatbotSubOptions.VIEW_RECOMMENDATIONS,
                                        ChatbotSubOptions.VIEW_PROMOTIONS,
                                        ChatbotSubOptions.BEST_VALUE,
                                        ChatbotSubOptions.BEST_SELLERS,
                                        ChatbotSubOptions.TOP_RATED,
                                        ChatbotSubOptions.NEW_ARRIVALS)),

        COMPARE_PRODUCTS(
                        "Compare Products",
                        List.of(
                                        ChatbotSubOptions.COMPARE_TWO_PRODUCTS,
                                        ChatbotSubOptions.WHICH_IS_BETTER,
                                        ChatbotSubOptions.VIEW_DIFFERENCES)),

        PRODUCT_INFORMATION(
                        "Product Information",
                        List.of(
                                        ChatbotSubOptions.VIEW_FEATURES,
                                        ChatbotSubOptions.VIEW_SPECIFICATIONS,
                                        ChatbotSubOptions.CHECK_AVAILABILITY,
                                        ChatbotSubOptions.VIEW_WARRANTY,
                                        ChatbotSubOptions.PAYMENT_OPTIONS,
                                        ChatbotSubOptions.RELATED_PRODUCTS)),

        ORDER_SUPPORT(
                        "Order Support",
                        List.of(
                                        ChatbotSubOptions.TRACK_ORDER,
                                        ChatbotSubOptions.SHIPPING_STATUS,
                                        ChatbotSubOptions.ESTIMATED_DELIVERY,
                                        ChatbotSubOptions.RETURNS_AND_REFUNDS)),

        CUSTOMER_SUPPORT(
                        "Customer Support",
                        List.of(
                                        ChatbotSubOptions.PAYMENT_ISSUES,
                                        ChatbotSubOptions.CANCELLATIONS,
                                        ChatbotSubOptions.CONTACT_AGENT));

        private final String label;
        private final List<ChatbotSubOptions> subOptions;

        ChatbotOptions(String label, List<ChatbotSubOptions> subOptions) {
                this.label = label;
                this.subOptions = subOptions;
        }

        public String getLabel() {
                return label;
        }

        public List<ChatbotSubOptions> getSubOptions() {
                return subOptions;
        }
}

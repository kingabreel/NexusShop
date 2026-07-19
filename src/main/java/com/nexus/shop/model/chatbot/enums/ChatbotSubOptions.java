package com.nexus.shop.model.chatbot.enums;

public enum ChatbotSubOptions {
    SEARCH_BY_NAME("Search by Name"),
    DESCRIBE_NEEDS("Describe Needs"),
    SEARCH_BY_CATEGORY("Search by Category"),
    SEARCH_BY_PRICE_RANGE("Search by Price Range"),
    VIEW_RECOMMENDATIONS("View Recommendations"),
    VIEW_PROMOTIONS("View Promotions"),

    BEST_VALUE("Best Value"),
    BEST_SELLERS("Best Sellers"),
    TOP_RATED("Top Rated"),
    NEW_ARRIVALS("New Arrivals"),
    SIMILAR_PRODUCTS("Similar Products"),

    COMPARE_TWO_PRODUCTS("Compare Two Products"),
    WHICH_IS_BETTER("Which is Better"),
    VIEW_DIFFERENCES("View Differences"),

    VIEW_FEATURES("View Features"),
    VIEW_SPECIFICATIONS("View Specifications"),
    CHECK_AVAILABILITY("Check Availability"),
    VIEW_WARRANTY("View Warranty Information"),
    PAYMENT_OPTIONS("Payment Options"),
    RELATED_PRODUCTS("Related Products"),

    TRACK_ORDER("Track Order"),
    SHIPPING_STATUS("Shipping Status"),
    ESTIMATED_DELIVERY("Estimated Delivery"),
    RETURNS_AND_REFUNDS("Returns and Refunds"),

    PAYMENT_ISSUES("Payment Issues"),
    CANCELLATIONS("Cancellations"),
    CONTACT_AGENT("Contact Agent");

    private final String label;

    ChatbotSubOptions(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
}

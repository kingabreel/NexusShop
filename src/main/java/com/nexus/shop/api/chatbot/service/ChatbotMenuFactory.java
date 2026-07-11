package com.nexus.shop.api.chatbot.service;

import java.util.List;

import com.nexus.shop.model.chatbot.entity.ChatbotMenuItem;

public final class ChatbotMenuFactory {

    private ChatbotMenuFactory() {
    }

    public static List<ChatbotMenuItem> build() {
        return List.of(

                new ChatbotMenuItem(
                        "FIND_PRODUCTS",
                        "Find Products",
                        List.of(
                                new ChatbotMenuItem("SEARCH_BY_NAME", "Search by Name", List.of()),
                                new ChatbotMenuItem("DESCRIBE_NEEDS", "Describe What I Need", List.of()),
                                new ChatbotMenuItem("SEARCH_BY_CATEGORY", "Search by Category", List.of()),
                                new ChatbotMenuItem("SEARCH_BY_PRICE", "Search by Price Range", List.of()),
                                new ChatbotMenuItem("VIEW_PROMOTIONS", "View Promotions", List.of()))),

                new ChatbotMenuItem(
                        "RECOMMENDATIONS",
                        "Recommendations",
                        List.of(
                                new ChatbotMenuItem("BEST_VALUE", "Best Value", List.of()),
                                new ChatbotMenuItem("BEST_SELLERS", "Best Sellers", List.of()),
                                new ChatbotMenuItem("TOP_RATED", "Top Rated", List.of()),
                                new ChatbotMenuItem("NEW_ARRIVALS", "New Arrivals", List.of()),
                                new ChatbotMenuItem("SIMILAR_PRODUCTS", "Similar Products", List.of()))),

                new ChatbotMenuItem(
                        "COMPARE_PRODUCTS",
                        "Compare Products",
                        List.of(
                                new ChatbotMenuItem("COMPARE_TWO_PRODUCTS", "Compare Two Products", List.of()),
                                new ChatbotMenuItem("WHICH_IS_BETTER", "Which Is Better for Me?", List.of()),
                                new ChatbotMenuItem("VIEW_DIFFERENCES", "View Differences", List.of()))),

                new ChatbotMenuItem(
                        "PRODUCT_INFORMATION",
                        "Product Information",
                        List.of(
                                new ChatbotMenuItem("VIEW_FEATURES", "Features", List.of()),
                                new ChatbotMenuItem("VIEW_SPECIFICATIONS", "Technical Specifications", List.of()),
                                new ChatbotMenuItem("CHECK_AVAILABILITY", "Availability", List.of()),
                                new ChatbotMenuItem("VIEW_WARRANTY", "Warranty", List.of()),
                                new ChatbotMenuItem("PAYMENT_OPTIONS", "Payment Options", List.of()),
                                new ChatbotMenuItem("RELATED_PRODUCTS", "Related Products", List.of()))),

                new ChatbotMenuItem(
                        "ORDER_SUPPORT",
                        "Orders & Shipping",
                        List.of(
                                new ChatbotMenuItem("TRACK_ORDER", "Track Order", List.of()),
                                new ChatbotMenuItem("SHIPPING_STATUS", "Shipping Status", List.of()),
                                new ChatbotMenuItem("ESTIMATED_DELIVERY", "Estimated Delivery", List.of()),
                                new ChatbotMenuItem("RETURNS_AND_REFUNDS", "Returns & Refunds", List.of()))),

                new ChatbotMenuItem(
                        "CUSTOMER_SUPPORT",
                        "Customer Support",
                        List.of(
                                new ChatbotMenuItem("PAYMENT_ISSUES", "Payment Issues", List.of()),
                                new ChatbotMenuItem("CANCELLATIONS", "Order Cancellation", List.of()),
                                new ChatbotMenuItem("CONTACT_AGENT", "Contact Support", List.of()))));
    }
}

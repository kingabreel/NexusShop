package com.nexus.shop.api.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexus.shop.api.analytics.service.ProductAnalyticService;
import com.nexus.shop.api.analytics.service.UserHistoryService;
import com.nexus.shop.api.embeddings.OnnxEmbeddingService;
import com.nexus.shop.api.rating.service.RatingService;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.dto.ProductPatchDTO;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.request.ProductCreateDTO;
import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.persistence.repository.UserRepository;
import com.nexus.shop.utils.helpers.ImageUploadHelper;

@ExtendWith(MockitoExtension.class)
class ProductServiceImageTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductAnalyticService productAnalyticService;

    @Mock
    private UserHistoryService userHistoryService;

    @Mock
    private RatingService ratingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OnnxEmbeddingService onnxEmbeddingService;

    @Mock
    private ImageUploadHelper imageUploadHelper;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(
                productRepository,
                productAnalyticService,
                userHistoryService,
                ratingService,
                userRepository,
                onnxEmbeddingService,
                imageUploadHelper);
    }

    @Test
    void shouldSaveProductImageWhenCreatingProduct() {
        User user = new User();
        user.setEmail("seller@email.com");
        user.setStore(mock(com.nexus.shop.model.store.entity.Store.class));

        when(userRepository.findByEmail(org.mockito.ArgumentMatchers.isNull())).thenReturn(Optional.of(user));
        when(imageUploadHelper.isValidBase64Image(anyString())).thenReturn(true);
        when(imageUploadHelper.saveImage(anyString())).thenReturn("https://cdn.local/products/123.jpg");
        when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductCreateDTO dto = new ProductCreateDTO(
                "Notebook",
                "Gaming",
                new BigDecimal("2999.90"),
                10,
                Category.ELETRONICOS,
                true,
                "data:image/jpeg;base64,AAAA");

        ProductResponseDTO response = productService.create(dto);

        assertNotNull(response);
        assertEquals("https://cdn.local/products/123.jpg", response.imageUrl());
    }

    @Test
    void shouldUpdateProductImageWhenPartialUpdateContainsNewImage() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Old name");
        product.setDescription("Old description");
        product.setPrice(new BigDecimal("10.00"));
        product.setStock(5);
        product.setCategory(Category.ELETRONICOS);
        product.setHighlight(false);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(imageUploadHelper.isValidBase64Image(anyString())).thenReturn(true);
        when(imageUploadHelper.saveImage(anyString())).thenReturn("https://cdn.local/products/456.jpg");
        when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductPatchDTO dto = new ProductPatchDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                "data:image/jpeg;base64,BBBB");

        ProductResponseDTO response = productService.updatePartial(product.getId(), dto);

        assertNotNull(response);
        assertEquals("https://cdn.local/products/456.jpg", response.imageUrl());
    }
}

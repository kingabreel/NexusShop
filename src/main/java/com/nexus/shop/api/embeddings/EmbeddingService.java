package com.nexus.shop.api.embeddings;

import com.nexus.shop.model.embeddings.EmbeddingResult;

public interface EmbeddingService {

    EmbeddingResult generate(String text);
}

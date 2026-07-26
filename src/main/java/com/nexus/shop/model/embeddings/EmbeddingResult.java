package com.nexus.shop.model.embeddings;

import lombok.Getter;

@Getter
public class EmbeddingResult {

    private final float[] vector;

    public EmbeddingResult(final float[] vector) {
        this.vector = vector;
    }
}

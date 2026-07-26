package com.nexus.shop.infra.exception;

public class EmbeddingGenerationException extends RuntimeException {

    public EmbeddingGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

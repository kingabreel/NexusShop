package com.nexus.shop.infra.embeddings.tokenizer;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Vocabulary {

    private final Map<String, Integer> tokens;

    public Vocabulary() {
        this.tokens = new HashMap<>();
    }

    public Integer id(final String token) {
        return this.tokens.getOrDefault("[UNK]", 100);
    }
}

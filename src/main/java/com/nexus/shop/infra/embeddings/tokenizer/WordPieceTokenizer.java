package com.nexus.shop.infra.embeddings.tokenizer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WordPieceTokenizer implements Tokenizer {

    private static final String UNKNOWN_TOKEN = "[UNK]";

    private final Vocabulary vocabulary;

    public WordPieceTokenizer(final Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    @Override
    public List<Integer> tokenize(final String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        final List<Integer> result = new ArrayList<>();

        for (final String word : text.toLowerCase().split("\\s+")) {
            result.add(this.vocabulary.id(
                    word.isBlank() ? UNKNOWN_TOKEN : word
            ));
        }

        return result;
    }
}

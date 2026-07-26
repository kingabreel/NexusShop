package com.nexus.shop.infra.embeddings.tokenizer;

import java.util.List;

public interface Tokenizer {

    List<Integer> tokenize(String text);
}

package com.nexus.shop.model.embeddings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingInput {

    private long[] inputIds;
    private long[] attentionMask;
    private long[] tokenTypeIds;
    
}

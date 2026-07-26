package com.nexus.shop.api.embeddings;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.shop.infra.embeddings.onnx.OnnxModel;
import com.nexus.shop.infra.embeddings.tokenizer.Tokenizer;
import com.nexus.shop.model.embeddings.EmbeddingInput;
import com.nexus.shop.model.embeddings.EmbeddingResult;

@Service
public class OnnxEmbeddingService implements EmbeddingService {

    private final Tokenizer tokenizer;
    private final OnnxModel onnxModel;
    private final PoolingService poolingService;
    private final NormalizationService normalizationService;

    public OnnxEmbeddingService(
            final Tokenizer tokenizer,
            final OnnxModel onnxModel,
            final PoolingService poolingService,
            final NormalizationService normalizationService) {
        this.tokenizer = tokenizer;
        this.onnxModel = onnxModel;
        this.poolingService = poolingService;
        this.normalizationService = normalizationService;
    }

    @Override
    public EmbeddingResult generate(final String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text cannot be empty");
        }

        final List<Integer> tokens = this.tokenizer.tokenize(text);

        final long[] ids = tokens.stream()
                .mapToLong(Integer::longValue)
                .toArray();

        final long[] mask = new long[ids.length];

        for (int index = 0; index < ids.length; index++) {
            mask[index] = 1;
        }

        long[] tokenTypeIds = new long[ids.length];

        final EmbeddingInput input = new EmbeddingInput(ids, mask, tokenTypeIds);

        final float[][][] output = this.onnxModel.execute(input);

        float[] embedding = this.poolingService.meanPool(
                output,
                mask);

        embedding = this.normalizationService.normalize(embedding);

        return new EmbeddingResult(embedding);
    }
}

package com.nexus.shop.api.embeddings;

import org.springframework.stereotype.Service;

@Service
public class PoolingService {

    public float[] meanPool(
            final float[][][] output,
            final long[] mask) {
        final float[][] tokens = output[0];

        final int dimension = tokens[0].length;

        final float[] result = new float[dimension];

        float count = 0;

        for (int token = 0; token < tokens.length; token++) {

            if (mask[token] == 0) {
                continue;
            }

            count++;

            for (int dimensionIndex = 0; dimensionIndex < dimension; dimensionIndex++) {
                result[dimensionIndex] += tokens[token][dimensionIndex];
            }
        }

        if (count == 0) {
            return result;
        }

        for (int dimensionIndex = 0; dimensionIndex < dimension; dimensionIndex++) {
            result[dimensionIndex] /= count;
        }

        return result;
    }
}

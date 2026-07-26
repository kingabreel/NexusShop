package com.nexus.shop.api.embeddings;

import org.springframework.stereotype.Service;

@Service
public class NormalizationService {

    public float[] normalize(final float[] vector) {

        double sum = 0;

        for (final float value : vector) {
            sum += value * value;
        }

        final double norm = Math.sqrt(sum);

        if (norm == 0) {
            return vector;
        }

        final float[] result = new float[vector.length];

        for (int i = 0; i < vector.length; i++) {
            result[i] = (float) (vector[i] / norm);
        }

        return result;
    }
}

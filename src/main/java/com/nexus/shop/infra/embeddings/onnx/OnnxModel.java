package com.nexus.shop.infra.embeddings.onnx;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.nexus.shop.infra.exception.EmbeddingGenerationException;
import com.nexus.shop.model.embeddings.EmbeddingInput;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;

@Component
public class OnnxModel {

    private final OrtEnvironment environment;
    private OrtSession session;

    public OnnxModel() {
        this.environment = OrtEnvironment.getEnvironment();
    }

    @PostConstruct
    public void init() {

        try {

            final ClassPathResource resource = new ClassPathResource(
                    "models/all-MiniLM-L6-V2.onnx");

            final byte[] modelBytes;

            try (var inputStream = resource.getInputStream()) {
                modelBytes = inputStream.readAllBytes();
            }

            final OrtSession.SessionOptions options = new OrtSession.SessionOptions();

            this.session = this.environment.createSession(
                    modelBytes,
                    options);

        } catch (Exception exception) {

            throw new EmbeddingGenerationException(
                    "Unable to load ONNX model",
                    exception);
        }
    }

    public float[][][] execute(final EmbeddingInput input) {

        try {

            final OnnxTensor ids = OnnxTensor.createTensor(
                    this.environment,
                    new long[][] { input.getInputIds() });

            final OnnxTensor mask = OnnxTensor.createTensor(
                    this.environment,
                    new long[][] { input.getAttentionMask() });

            OnnxTensor tokenTypes = OnnxTensor.createTensor(
                    this.environment,
                    new long[][] { input.getTokenTypeIds() });

            OrtSession.Result results = session.run(
                    Map.of(
                            "input_ids", ids,
                            "attention_mask", mask,
                            "token_type_ids", tokenTypes));
            return (float[][][]) results.get(0).getValue();

        } catch (final OrtException exception) {

            throw new EmbeddingGenerationException(
                    "Embedding generation failed",
                    exception);
        }
    }
}

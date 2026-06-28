package com.nexus.shop.infra.external;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CohereApiCall {

    private static final String COHERE_URL = "https://api.cohere.com/v1/embed";

    private final HttpClient httpClient;

    @Value("${cohere.api.key}")
    private String apiKey;

    public CohereApiCall() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<float[]> generateEmbeddings(final List<String> texts) {
        try {
            final JSONObject payload = new JSONObject();
            payload.put("model", "embed-v4.0");
            payload.put("texts", texts);
            payload.put("input_type", "search_document");

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(COHERE_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            final HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Cohere API error: " + response.body());
            }

            return parseEmbeddings(response.body());

        } catch (IOException | InterruptedException | RuntimeException e) {
            log.error("Error calling Cohere embeddings API", e);
            throw new RuntimeException(e);
        }
    }

    private List<float[]> parseEmbeddings(final String responseBody) {
        final JSONObject json = new JSONObject(responseBody);
        final JSONArray embeddings = json.getJSONArray("embeddings");

        final List<float[]> result = new ArrayList<>();

        for (int i = 0; i < embeddings.length(); i++) {
            final JSONArray vector = embeddings.getJSONArray(i);

            final float[] arr = new float[vector.length()];
            for (int j = 0; j < vector.length(); j++) {
                arr[j] = vector.getFloat(j);
            }

            result.add(arr);
        }

        return result;
    }
}

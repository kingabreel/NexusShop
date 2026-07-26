package com.nexus.shop.infra.embeddings.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "embedding")
public class EmbeddingProperties {

    private String modelPath;
    private String vocabularyPath;
    private Integer maxLength = 128;
    private Integer dimension = 384;
}

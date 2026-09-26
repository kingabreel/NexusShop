package com.nexus.shop.utils.helpers;

import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nexus.shop.persistence.cdn.CdnInterface;
import com.nexus.shop.persistence.cdn.LocalImpl;
import com.nexus.shop.persistence.cdn.MinioImpl;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;

@Service
public class ImageUploadHelper {

    private final MinioClient minioClient;
    private CdnInterface cdnService;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url}")
    private String url;

    @Value("${local.cdn.activate:true}")
    private boolean localCdnActivate;

    @Value("${local.cdn.path:./cdn}")
    private String localCdnPath;

    public ImageUploadHelper(final MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    private void initCdn() {
        if (this.localCdnActivate) {
            this.cdnService = new LocalImpl(this.localCdnPath);
        } else {
            this.cdnService = new MinioImpl(minioClient, this.bucketName, this.url);
        }
    }

    public String saveImage(final String base64Content) {
        String filename = UUID.randomUUID().toString() + ".jpg";
        byte[] fileContent = decodeBase64(base64Content);
        return cdnService.uploadFile(filename, fileContent);
    }

    public boolean isValidBase64Image(final String base64) {
        try {
            String content = stripBase64Prefix(base64);
            if (content.length() % 4 != 0) return false;
            byte[] decoded = Base64.getDecoder().decode(content);
            if (decoded.length < 4) return false;
            boolean isPng = decoded[0] == (byte) 0x89 && decoded[1] == 0x50;
            boolean isJpg = decoded[0] == (byte) 0xFF && decoded[1] == (byte) 0xD8;
            return isPng || isJpg;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private byte[] decodeBase64(String base64Content) {
        base64Content = stripBase64Prefix(base64Content);
        return Base64.getDecoder().decode(base64Content);
    }

    private String stripBase64Prefix(String base64Content) {
        if (base64Content.contains(",")) {
            return base64Content.split(",")[1];
        }
        return base64Content;
    }
}

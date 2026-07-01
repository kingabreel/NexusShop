package com.nexus.shop.persistence.cdn;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

@Service
public class MinioImpl implements CdnInterface {

    private final MinioClient minioClient;
    private final String bucketName;
    private final String url;

    public MinioImpl(
            MinioClient minioClient,
            @Value("${minio.bucket}") String bucketName,
            @Value("${minio.url}") String url) {

        this.minioClient = minioClient;
        this.bucketName = bucketName;
        this.url = url;
    }

    @Override
    public String uploadFile(String fileName, byte[] fileContent) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(new ByteArrayInputStream(fileContent), fileContent.length, -1)
                            .build()
            );

            return url + "/" + bucketName + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to MinIO", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from MinIO", e);
        }
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        )) {
            return stream.readAllBytes();

        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from MinIO", e);
        }
    }
}

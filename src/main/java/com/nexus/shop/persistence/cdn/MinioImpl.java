package com.nexus.shop.persistence.cdn;

public class MinioImpl implements CdnInterface {
    @Override
    public String uploadFile(String fileName, byte[] fileContent) {
        // Implement the logic to upload the file to Minio
        return null;
    }

    @Override
    public void deleteFile(String fileName) {
        // Implement the logic to delete the file from Minio
    }

    @Override
    public byte[] downloadFile(String fileName) {
        // Implement the logic to download the file from Minio
        return null;
    }
    
}

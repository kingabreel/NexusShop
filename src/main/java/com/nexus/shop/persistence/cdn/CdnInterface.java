package com.nexus.shop.persistence.cdn;

public interface CdnInterface {
    String uploadFile(String fileName, byte[] fileContent);

    void deleteFile(String fileName);

    byte[] downloadFile(String fileName);
    
}

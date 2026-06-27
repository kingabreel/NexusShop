package com.nexus.shop.api.rating.controller;

// TODO: Remover (talvez)
public class ImageUploadRequest {

    private String filename;
    private String base64;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}

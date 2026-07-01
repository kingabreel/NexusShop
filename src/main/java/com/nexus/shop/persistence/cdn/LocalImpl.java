package com.nexus.shop.persistence.cdn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class LocalImpl implements CdnInterface {

    private final Path basePath;

    public LocalImpl(final String basePath) {
        this.basePath = Paths.get(basePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.basePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize local CDN directory: " + basePath, e);
        }
    }

    @Override
    public String uploadFile(final String fileName, final byte[] fileContent) {
        try {
            final Path filePath = resolvePath(fileName);

            Files.createDirectories(filePath.getParent());

            Files.write(
                filePath,
                fileContent,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );

            return filePath.toString();

        } catch (final IOException e) {
            throw new RuntimeException("Error uploading file locally: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(final String fileName) {
        try {
            final Path filePath = resolvePath(fileName);
            Files.deleteIfExists(filePath);
        } catch (final IOException e) {
            throw new RuntimeException("Error deleting file locally: " + fileName, e);
        }
    }

    @Override
    public byte[] downloadFile(final String fileName) {
        try {
            final Path filePath = resolvePath(fileName);

            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + fileName);
            }

            return Files.readAllBytes(filePath);

        } catch (final IOException e) {
            throw new RuntimeException("Error downloading file locally: " + fileName, e);
        }
    }

    private Path resolvePath(final String fileName) {
        final Path normalized = Paths.get(fileName).normalize();

        if (normalized.startsWith("..")) {
            throw new IllegalArgumentException("Invalid file path: " + fileName);
        }

        return basePath.resolve(normalized).normalize();
    }
}

package com.example.user.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class TestUtils {

    private TestUtils() {
    }

    public static String readStringFromResource(String resourcePath) {
        try (InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + resourcePath, e);
        }
    }
} 
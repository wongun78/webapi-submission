package com.example.webapi.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    /**
     * Convert Vietnamese text to slug format
     * Example: "Tin Tức Công Nghệ" -> "tin-tuc-cong-nghe"
     */
    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Convert to lowercase
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        
        // Remove accents from Vietnamese characters
        slug = slug.toLowerCase()
            .replaceAll("đ", "d")
            .replaceAll("Đ", "d")
            // Multiple dashes to single dash
            .replaceAll("-+", "-")
            // Remove leading/trailing dashes
            .replaceAll("^-|-$", "");
        
        return slug;
    }
}

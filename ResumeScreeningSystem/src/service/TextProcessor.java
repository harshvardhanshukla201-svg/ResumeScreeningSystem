package service;

import java.util.*;

public class TextProcessor {

    private static final Set<String> STOPWORDS = Set.of(
            "is","the","and","a","to","in","of","for","on","with"
    );

    public static List<String> process(String text) {
        text = text.toLowerCase();
        String[] words = text.replaceAll("[^a-z ]", "").split("\\s+");

        List<String> tokens = new ArrayList<>();
        for (String word : words) {
            if (!STOPWORDS.contains(word) && !word.isBlank()) {
                tokens.add(word);
            }
        }
        return tokens;
    }
}
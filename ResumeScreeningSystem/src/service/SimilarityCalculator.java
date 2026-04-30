package service;

import java.util.*;

public class SimilarityCalculator {

    public static double cosineSimilarity(Map<String, Double> v1,
                                          Map<String, Double> v2) {

        Set<String> allWords = new HashSet<>();
        allWords.addAll(v1.keySet());
        allWords.addAll(v2.keySet());

        double dotProduct = 0, mag1 = 0, mag2 = 0;

        for (String word : allWords) {
            double x = v1.getOrDefault(word, 0.0);
            double y = v2.getOrDefault(word, 0.0);

            dotProduct += x * y;
            mag1 += x * x;
            mag2 += y * y;
        }

        return dotProduct / (Math.sqrt(mag1) * Math.sqrt(mag2) + 1e-10);
    }
}
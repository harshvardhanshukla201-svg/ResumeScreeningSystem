package service;

import java.util.*;

public class TFIDFVectorizer {

    public static Map<String, Double> computeTF(List<String> tokens) {
        Map<String, Double> tf = new HashMap<>();
        int total = tokens.size();

        for (String word : tokens) {
            tf.put(word, tf.getOrDefault(word, 0.0) + 1.0);
        }

        for (String word : tf.keySet()) {
            tf.put(word, tf.get(word) / total);
        }

        return tf;
    }

    public static Map<String, Double> computeIDF(List<List<String>> documents) {
        Map<String, Double> idf = new HashMap<>();
        int totalDocs = documents.size();

        Set<String> allWords = new HashSet<>();
        for (List<String> doc : documents) {
            allWords.addAll(doc);
        }

        for (String word : allWords) {
            int count = 0;
            for (List<String> doc : documents) {
                if (doc.contains(word)) count++;
            }
            idf.put(word, Math.log((double) totalDocs / (1 + count)));
        }

        return idf;
    }

    public static Map<String, Double> computeTFIDF(
            Map<String, Double> tf,
            Map<String, Double> idf) {

        Map<String, Double> tfidf = new HashMap<>();

        for (String word : tf.keySet()) {
            tfidf.put(word, tf.get(word) * idf.getOrDefault(word, 0.0));
        }

        return tfidf;
    }
}
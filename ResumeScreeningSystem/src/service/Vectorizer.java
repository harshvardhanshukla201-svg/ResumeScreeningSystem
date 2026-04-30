package service;

import java.util.*;

public class Vectorizer {

    public static Map<String, Integer> getTermFrequency(List<String> tokens) {
        Map<String, Integer> tf = new HashMap<>();

        for (String word : tokens) {
            tf.put(word, tf.getOrDefault(word, 0) + 1);
        }

        return tf;
    }
}
package service;

import model.Resume;
import java.util.*;

public class Ranker {

    public static List<Resume> rank(List<Resume> resumes) {
        resumes.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        return resumes;
    }

    public static List<Resume> getTopCandidates(List<Resume> resumes, int n) {
        return resumes.stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(n)
                .toList();
    }
}
package main;

import model.Resume;
import service.*;
import utils.FileReaderUtil;

import java.util.*;

public class Main {

    private static List<Resume> resumes = new ArrayList<>();
    private static Map<String, Double> idf;
    private static Map<String, Double> jdVector;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n***Resume Screening System***");
            System.out.println("1. Load & Rank Resumes");
            System.out.println("2. Show Top 3 Candidates");
            System.out.println("3. Show All Scores");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> loadAndRank();
                case 2 -> showTopCandidates();
                case 3 -> showAllScores();
                case 4 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void loadAndRank() {

        if (!resumes.isEmpty()) {
            System.out.println("Resumes already loaded. Reloading...");
        }

        resumes.clear();

        String jobDesc = FileReaderUtil.readFile("data/job_description.txt");
        Map<String, String> resumeData =
                FileReaderUtil.readResumesFromFolder("data/resumes");

        List<List<String>> allDocuments = new ArrayList<>();

        List<String> jdTokens = TextProcessor.process(jobDesc);
        allDocuments.add(jdTokens);

        for (Map.Entry<String, String> entry : resumeData.entrySet()) {
            List<String> tokens = TextProcessor.process(entry.getValue());
            allDocuments.add(tokens);
            resumes.add(new Resume(entry.getKey(), entry.getValue()));
        }

        idf = TFIDFVectorizer.computeIDF(allDocuments);

        Map<String, Double> jdTF = TFIDFVectorizer.computeTF(jdTokens);
        jdVector = TFIDFVectorizer.computeTFIDF(jdTF, idf);

        int index = 1;
        for (Resume r : resumes) {
            List<String> tokens = allDocuments.get(index++);
            Map<String, Double> tf = TFIDFVectorizer.computeTF(tokens);
            Map<String, Double> vector = TFIDFVectorizer.computeTFIDF(tf, idf);

            double score = SimilarityCalculator.cosineSimilarity(jdVector, vector);
            r.setScore(score);
        }

        System.out.println("Resumes loaded and ranked successfully!");
    }

    private static void showTopCandidates() {
        if (resumes.isEmpty()) {
            System.out.println("Please load resumes first!");
            return;
        }

        List<Resume> top = Ranker.getTopCandidates(resumes, 3);

        System.out.println("\nTop Candidates:");
        for (Resume r : top) {
            System.out.printf("%-15s -> %.4f (%s)%n",
                    r.getName(),
                    r.getScore(),
                    getLabel(r.getScore()));
        }
    }

    private static void showAllScores() {
        if (resumes.isEmpty()) {
            System.out.println("Please load resumes first!");
            return;
        }

        List<Resume> ranked = Ranker.rank(resumes);

        System.out.println("\nAll Candidates:");
        for (Resume r : ranked) {
            System.out.printf("%-15s -> %.4f (%s)%n",
                    r.getName(),
                    r.getScore(),
                    getLabel(r.getScore()));
        }
    }

    private static String getLabel(double score) {
        if (score > 0.15) return "Strong Match";
        else if (score > 0.05) return "Moderate Match";
        else return "Weak Match";
    }
}
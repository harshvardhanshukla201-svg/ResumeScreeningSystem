package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import model.Resume;
import service.*;
import utils.FileReaderUtil;

import java.util.*;

public class AppUI extends Application {

    private List<Resume> resumes = new ArrayList<>();
    private TextArea outputArea;

    @Override
    public void start(Stage stage) {

        // Title
        Label title = new Label("Resume Screening System");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Buttons
        Button loadBtn = new Button("Load & Rank Resumes");
        Button topBtn = new Button("Show Top 3");
        Button allBtn = new Button("Show All");

        loadBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        topBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        allBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");

        // Output area
        outputArea = new TextArea();
        outputArea.setPrefHeight(300);
        outputArea.setStyle("-fx-font-family: Consolas; -fx-font-size: 13px;");

        // Button actions
        loadBtn.setOnAction(e -> loadAndRank());
        topBtn.setOnAction(e -> showTop());
        allBtn.setOnAction(e -> showAll());

        // Layout
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #f4f6f8;");
        layout.getChildren().addAll(title, loadBtn, topBtn, allBtn, outputArea);

        // Scene
        stage.setTitle("Resume Screening System");
        stage.setScene(new Scene(layout, 500, 420));
        stage.show();
    }

    private void loadAndRank() {

        resumes.clear();

        String jobDesc = FileReaderUtil.readFile("data/job_description.txt");
        Map<String, String> resumeData =
                FileReaderUtil.readResumesFromFolder("data/resumes");

        List<List<String>> allDocs = new ArrayList<>();
        List<String> jdTokens = TextProcessor.process(jobDesc);
        allDocs.add(jdTokens);

        for (Map.Entry<String, String> entry : resumeData.entrySet()) {
            List<String> tokens = TextProcessor.process(entry.getValue());
            allDocs.add(tokens);
            resumes.add(new Resume(entry.getKey(), entry.getValue()));
        }

        Map<String, Double> idf = TFIDFVectorizer.computeIDF(allDocs);

        Map<String, Double> jdTF = TFIDFVectorizer.computeTF(jdTokens);
        Map<String, Double> jdVector = TFIDFVectorizer.computeTFIDF(jdTF, idf);

        int index = 1;
        for (Resume r : resumes) {
            List<String> tokens = allDocs.get(index++);
            Map<String, Double> tf = TFIDFVectorizer.computeTF(tokens);
            Map<String, Double> vector = TFIDFVectorizer.computeTFIDF(tf, idf);

            double score = SimilarityCalculator.cosineSimilarity(jdVector, vector);
            r.setScore(score);
        }

        outputArea.setText("Resumes loaded and ranked successfully!");
    }

    private void showTop() {
        if (resumes.isEmpty()) {
            outputArea.setText("Please load resumes first!");
            return;
        }

        List<Resume> top = Ranker.getTopCandidates(resumes, 3);

        StringBuilder sb = new StringBuilder();
        sb.append("Top Candidates:\n\n");
        sb.append("Total Resumes: ").append(resumes.size()).append("\n\n");

        for (Resume r : top) {
            sb.append(String.format("%-15s -> %.4f (%s)\n",
                    r.getName(),
                    r.getScore(),
                    getLabel(r.getScore())));
        }

        outputArea.setText(sb.toString());
    }

    private void showAll() {
        if (resumes.isEmpty()) {
            outputArea.setText("Please load resumes first!");
            return;
        }

        List<Resume> ranked = Ranker.rank(resumes);

        StringBuilder sb = new StringBuilder();
        sb.append("All Candidates:\n\n");
        sb.append("Total Resumes: ").append(resumes.size()).append("\n\n");

        for (Resume r : ranked) {
            sb.append(String.format("%-15s -> %.4f (%s)\n",
                    r.getName(),
                    r.getScore(),
                    getLabel(r.getScore())));
        }

        outputArea.setText(sb.toString());
    }

    private String getLabel(double score) {
        if (score > 0.15) return "Strong Match";
        else if (score > 0.05) return "Moderate Match";
        else return "Weak Match";
    }

    public static void main(String[] args) {
        launch();
    }
}
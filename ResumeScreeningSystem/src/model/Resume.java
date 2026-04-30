package model;

public class Resume {
    private String name;
    private String content;
    private double score;

    public Resume(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() { return name; }
    public String getContent() { return content; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
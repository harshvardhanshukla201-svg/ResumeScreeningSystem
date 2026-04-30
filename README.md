# ResumeScreeningSystem

About:

This is a Java-based project that ranks resumes based on how well they match a given job description.
The idea is simple: instead of manually checking resumes, the system compares each resume with the job description and gives a score. Higher score means better match.

Functionality:

1) Reads resumes from a folder
2) Reads a job description
3) Processes the text (removes extra words, cleans data)
4) Converts text into numerical form using TF-IDF
5) Calculates similarity using cosine similarity
6) Ranks resumes based on the score
7) Shows results in CLI and a simple JavaFX UI

Tech used:
1) Java
2) JavaFX
3) Basic NLP concepts
4) TF-IDF
5) Cosine Similarity

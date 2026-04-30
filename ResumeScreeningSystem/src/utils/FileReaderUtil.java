package utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileReaderUtil {

    public static String readFile(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Error reading file: " + path);
            return "";
        }
    }

    public static Map<String, String> readResumesFromFolder(String folderPath) {
        Map<String, String> resumes = new HashMap<>();

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(folderPath));

            for (Path file : stream) {
                String content = Files.readString(file);
                String fileName = file.getFileName().toString().replace(".txt", "");
                resumes.put(fileName, content);
            }

        } catch (IOException e) {
            System.out.println("Error reading folder: " + folderPath);
        }

        return resumes;
    }
}
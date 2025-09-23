package com.kodilla;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RankingManager {
    private final Path filePath = Paths.get("ranking.txt");

    public void saveResult(GameResult result) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(result.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Błąd zapisu: " + e);
        }
    }

    public List<GameResult> loadResults() {
        List<GameResult> results = new ArrayList<>();
        if (!Files.exists(filePath)) return results;

        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(line -> {
                try {
                    results.add(GameResult.fromFileString(line));
                } catch (Exception e) {
                    System.out.println("Błąd parsowania linii: " + line);
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("Błąd odczytu: " + e);
        }

        return results;
    }

    public void clearResults() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.TRUNCATE_EXISTING)) {
        } catch (IOException e) {
            System.out.println("Błąd przy czyszczeniu: " + e);
        }
    }
}


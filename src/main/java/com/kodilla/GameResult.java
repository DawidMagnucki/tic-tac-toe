package com.kodilla;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GameResult {
    private final String username;
    private final int gamesPlayed;
    private final int gamesWon;
    private final LocalDate date;

    public GameResult(String username, int gamesPlayed, int gamesWon, LocalDate date) {
        this.username = username;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public LocalDate getDate() {
        return date;
    }

    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        return date.format(formatter) + " - " + username + " - " + gamesPlayed + " - " + gamesWon;
    }


    public static GameResult fromFileString(String line) {
        String[] parts = line.split(" - ");
        LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("dd-MM-yy"));
        String username = parts[1];
        int gamesPlayed = Integer.parseInt(parts[2]);
        int gamesWon = Integer.parseInt(parts[3]);
        return new GameResult(username, gamesPlayed, gamesWon, date);
    }

}


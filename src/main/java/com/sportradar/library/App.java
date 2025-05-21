package com.sportradar.library;

import com.sportradar.library.match.MatchSummary;
import com.sportradar.library.scoreboard.ScoreBoardImpl;
import com.sportradar.library.scoreboard.Scoreboard;

import java.util.List;

public class App {
    public static void main(String[] args) {

        Scoreboard scoreBoard = new ScoreBoardImpl();

        scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", "Canada", 0, 5);

        scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);

        scoreBoard.startMatch("Germany", "France");
        scoreBoard.updateScore("Germany", "France", 2, 2);

        scoreBoard.startMatch("Uruguay", "Italy");
        scoreBoard.updateScore("Uruguay", "Italy", 6, 6);

        scoreBoard.startMatch("Argentina", "Australia");
        scoreBoard.updateScore("Argentina", "Australia", 3, 1);

        scoreBoard.startMatch("Germany", "Italy");
        scoreBoard.finishMatch("Germany", "Italy");

        List<MatchSummary> matchesSummary = scoreBoard.getOrderedMatches();

        System.out.println("\nMatches in progress:");
        for (MatchSummary summary : matchesSummary) {
            String line = String.format(
                    "%s %2d - %s %2d",
                    summary.homeTeamName(),
                    summary.homeTeamScore(),
                    summary.awayTeamName(),
                    summary.awayTeamScore()
            );
            System.out.println(line);
        }


    }
}

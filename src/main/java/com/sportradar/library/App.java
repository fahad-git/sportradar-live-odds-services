package com.sportradar.library;

import com.sportradar.library.match.Match;
import com.sportradar.library.scoreboard.ScoreBoardImpl;
import com.sportradar.library.scoreboard.Scoreboard;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

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

        List<Match> matchesInProgress = scoreBoard.getOrderedMatches();

        System.out.println("\nMatches in progress:");
        for (Match match : matchesInProgress) {
            String summary = String.format("%s %2d - %s %2d", match.getHomeTeam().getName(), match.getHomeTeam().getScore(), match.getAwayTeam().getName(), match.getAwayTeam().getScore());
            System.out.println(summary);
        }

    }
}

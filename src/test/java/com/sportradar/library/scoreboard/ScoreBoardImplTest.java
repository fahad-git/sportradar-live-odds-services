package com.sportradar.library.scoreboard;

import com.sportradar.library.exceptions.ScoreBoardException;
import com.sportradar.library.exceptions.TeamNameException;
import com.sportradar.library.match.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.jdi.request.DuplicateRequestException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardImplTest {

    private ScoreBoardImpl scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = new ScoreBoardImpl();
    }

    @Test
    void shouldStartMatchSuccessfully() {
        scoreboard.startMatch("TeamA", "TeamB");
        List<Match> matches = scoreboard.getOrderedMatches();

        assertEquals(1, matches.size());
        assertEquals("TeamA", matches.get(0).getHomeTeam().getName());
        assertEquals("TeamB", matches.get(0).getAwayTeam().getName());
    }

    @Test
    void shouldThrowExceptionForDuplicateMatch() {
        scoreboard.startMatch("TeamA", "TeamB");
        assertThrows(ScoreBoardException.class, () ->
                scoreboard.startMatch("TeamA", "TeamB"));
    }

    @Test
    void shouldThrowExceptionForBlankOrSameTeamNames() {
        assertAll("Invalid team names",
                () -> assertThrows(TeamNameException.class, () -> scoreboard.startMatch("  ", "TeamB")),
                () -> assertThrows(TeamNameException.class, () -> scoreboard.startMatch("TeamA", null)),
                () -> assertThrows(TeamNameException.class, () -> scoreboard.startMatch("TeamA", "TeamA"))
        );
    }

    @Test
    void shouldFinishMatchSuccessfully() {
        scoreboard.startMatch("TeamA", "TeamB");
        scoreboard.finishMatch("TeamA", "TeamB");

        List<Match> matches = scoreboard.getOrderedMatches();
        assertTrue(matches.isEmpty());
    }

    @Test
    void shouldUpdateScoreSuccessfully() {
        scoreboard.startMatch("TeamA", "TeamB");
        scoreboard.updateScore("TeamA", "TeamB", 2, 3);

        Match match = scoreboard.getOrderedMatches().get(0);
        assertEquals(2, match.getHomeTeam().getScore());
        assertEquals(3, match.getAwayTeam().getScore());
        assertEquals(5, match.getTotalScore());
    }

    @Test
    void shouldThrowExceptionForNegativeScores() {
        scoreboard.startMatch("TeamA", "TeamB");
        assertThrows(IllegalArgumentException.class, () ->
                scoreboard.updateScore("TeamA", "TeamB", -1, 2));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingMatch() {
        assertThrows(ScoreBoardException.class, () ->
                scoreboard.updateScore("TeamX", "TeamY", 1, 1));
    }

    @Test
    void shouldReturnMatchesOrderedByTotalScoreAndStartTime() throws InterruptedException {
        scoreboard.startMatch("Mexico", "Canada");      // 0
        Thread.sleep(10); // Ensuring different start times
        scoreboard.startMatch("Spain", "Brazil");       // 0
        Thread.sleep(10);
        scoreboard.startMatch("Germany", "France");     // 0
        Thread.sleep(10);
        scoreboard.startMatch("Uruguay", "Italy");      // 0
        Thread.sleep(10);
        scoreboard.startMatch("Argentina", "Australia");// 0

        scoreboard.updateScore("Mexico", "Canada", 0, 5);       // 5
        scoreboard.updateScore("Spain", "Brazil", 10, 2);       // 12
        scoreboard.updateScore("Germany", "France", 2, 2);      // 4
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);       // 12
        scoreboard.updateScore("Argentina", "Australia", 3, 1); // 4

        List<Match> ordered = scoreboard.getOrderedMatches();

        assertEquals("Uruguay", ordered.get(0).getHomeTeam().getName()); // 12, newer
        assertEquals("Spain", ordered.get(1).getHomeTeam().getName());   // 12, older
        assertEquals("Mexico", ordered.get(2).getHomeTeam().getName());  // 5
        assertEquals("Argentina", ordered.get(3).getHomeTeam().getName()); // 4, newer
        assertEquals("Germany", ordered.get(4).getHomeTeam().getName()); // 4, older
    }
}

package com.sportradar.library.scoreboard;

import com.sportradar.library.exceptions.DuplicateMatchException;
import com.sportradar.library.exceptions.MatchNotFoundException;
import com.sportradar.library.exceptions.TeamNameException;
import com.sportradar.library.match.MatchSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardImplTest {

    private ScoreBoardImpl scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = new ScoreBoardImpl();
    }

    private int getTotalScore(MatchSummary summary) {
        return summary.homeTeamScore() + summary.awayTeamScore();
    }

    @Test
    void shouldStartMatchSuccessfully() {
        scoreboard.startMatch("TeamA", "TeamB");
        List<MatchSummary> matches = scoreboard.getOrderedMatches();

        assertEquals(1, matches.size());
        assertEquals("TeamA", matches.get(0).homeTeamName());
        assertEquals("TeamB", matches.get(0).awayTeamName());
    }

    @Test
    void shouldThrowForDuplicateMatch() {
        scoreboard.startMatch("TeamA", "TeamB");
        DuplicateMatchException ex = assertThrows(DuplicateMatchException.class, () ->
                scoreboard.startMatch("TeamA", "TeamB"));
        assertEquals("It is not possible to have two same matches at the same time.", ex.getMessage());
    }

    @Test
    void shouldThrowForBlankOrSameTeamNames() {
        assertAll("Invalid team names",
                () -> {
                    TeamNameException ex = assertThrows(TeamNameException.class, () ->
                            scoreboard.startMatch("  ", "TeamB"));
                    assertEquals("Team name cannot be empty.", ex.getMessage());
                },
                () -> {
                    TeamNameException ex = assertThrows(TeamNameException.class, () ->
                            scoreboard.startMatch("TeamA", null));
                    assertEquals("Team name cannot be empty.", ex.getMessage());
                },
                () -> {
                    TeamNameException ex = assertThrows(TeamNameException.class, () ->
                            scoreboard.startMatch("TeamA", "TeamA"));
                    assertEquals("Teams cannot have the same name.", ex.getMessage());
                }
        );
    }

    @Test
    void shouldFinishMatchSuccessfully() {
        scoreboard.startMatch("TeamA", "TeamB");
        scoreboard.finishMatch("TeamA", "TeamB");

        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        assertTrue(matches.isEmpty());
    }

    @Test
    void shouldThrowWhenFinishingNonExistingMatch() {
        MatchNotFoundException ex = assertThrows(MatchNotFoundException.class, () ->
                scoreboard.finishMatch("UnknownA", "UnknownB"));
        assertEquals("Cannot finish match that doesn't exist.", ex.getMessage());
    }

    @Test
    void shouldUpdateScoreSuccessfully() {
        scoreboard.startMatch("TeamA", "TeamB");
        scoreboard.updateScore("TeamA", "TeamB", 2, 3);

        MatchSummary match = scoreboard.getOrderedMatches().get(0);
        assertEquals(2, match.homeTeamScore());
        assertEquals(3, match.awayTeamScore());
        assertEquals(5, getTotalScore(match));
    }

    @Test
    void shouldThrowForNegativeScores() {
        scoreboard.startMatch("TeamA", "TeamB");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                scoreboard.updateScore("TeamA", "TeamB", -1, 2));
        assertEquals("It is not possible to update match with negative values.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingMatch() {
        MatchNotFoundException ex = assertThrows(MatchNotFoundException.class, () ->
                scoreboard.updateScore("TeamX", "TeamY", 1, 1));
        assertEquals("It is not possible to update match which is not active.", ex.getMessage());
    }

    @Test
    void shouldReturnMatchesOrderedByTotalScoreAndStartTime() throws InterruptedException {
        scoreboard.startMatch("Mexico", "Canada");      // 0
        Thread.sleep(10);
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

        List<MatchSummary> ordered = scoreboard.getOrderedMatches();

        assertEquals("Uruguay", ordered.get(0).homeTeamName());   // 12, newer
        assertEquals("Spain", ordered.get(1).homeTeamName());     // 12, older
        assertEquals("Mexico", ordered.get(2).homeTeamName());    // 5
        assertEquals("Argentina", ordered.get(3).homeTeamName()); // 4, newer
        assertEquals("Germany", ordered.get(4).homeTeamName());   // 4, older

        // Optional score checks
        assertEquals(12, getTotalScore(ordered.get(0)));
        assertEquals(12, getTotalScore(ordered.get(1)));
        assertEquals(5, getTotalScore(ordered.get(2)));
        assertEquals(4, getTotalScore(ordered.get(3)));
        assertEquals(4, getTotalScore(ordered.get(4)));
    }
}

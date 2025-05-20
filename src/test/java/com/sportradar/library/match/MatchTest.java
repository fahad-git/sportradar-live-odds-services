package com.sportradar.library.match;

import com.sportradar.library.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MatchImplTest {

    private Team homeTeam;
    private Team awayTeam;
    private MatchImpl match;

    @BeforeEach
    void setUp() {
        homeTeam = new Team("TeamA");
        awayTeam = new Team("TeamB");
        match = new MatchImpl(homeTeam, awayTeam);
    }

    @Test
    void shouldInitializeTeamsCorrectly() {
        assertAll("Team Initialization",
                () -> assertEquals("TeamA", match.getHomeTeam().getName()),
                () -> assertEquals("TeamB", match.getAwayTeam().getName())
        );
    }

    @Test
    void shouldInitializeScoresToZero() {
        assertAll("Initial Scores",
                () -> assertEquals(0, match.getHomeTeam().getScore()),
                () -> assertEquals(0, match.getAwayTeam().getScore())
        );
    }

    @Test
    void shouldSetStartTimeAtConstruction() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = match.getStartTime();

        assertAll("Start Time Check",
                () -> assertTrue(startTime.isBefore(now.plusSeconds(1))),
                () -> assertTrue(startTime.isAfter(now.minusSeconds(1)))
        );
    }

    @Test
    void totalScoreShouldBeZeroInitially() {
        assertEquals(0, match.getTotalScore());
    }

    @Test
    void shouldUpdateScoresCorrectly() {
        match.updateScore(2, 3);

        assertAll("Score Update",
                () -> assertEquals(2, match.getHomeTeam().getScore()),
                () -> assertEquals(3, match.getAwayTeam().getScore()),
                () -> assertEquals(5, match.getTotalScore())
        );
    }

    @Test
    void shouldAllowNegativeScores() {
        match.updateScore(-1, -2);

        assertAll("Negative Score Handling",
                () -> assertEquals(-1, match.getHomeTeam().getScore()),
                () -> assertEquals(-2, match.getAwayTeam().getScore()),
                () -> assertEquals(-3, match.getTotalScore())
        );
    }
}

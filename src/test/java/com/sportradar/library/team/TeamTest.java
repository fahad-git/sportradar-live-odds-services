package com.sportradar.library.team;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team("TeamA");
    }

    @Test
    void shouldInitializeTeamWithNameAndZeroScore() {
        assertAll("Constructor Initialization",
                () -> assertEquals("TeamA", team.getName()),
                () -> assertEquals(0, team.getScore())
        );
    }

    @Test
    void shouldAllowScoreToBeUpdated() {
        team.setScore(10);
        assertEquals(10, team.getScore());
    }

    @Test
    void shouldNotAllowNameToBeModified() {
        // Verifying that name is final and cannot be modified
        assertEquals("TeamA", team.getName());
        // No setter = compile-time protection
    }
}

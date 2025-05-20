package com.sportradar.library.match;

import com.sportradar.library.team.Team;

import java.time.LocalDateTime;

public interface Match {
    int getTotalScore();

    Team getHomeTeam();

    Team getAwayTeam();

    void updateScore(int homeScore, int awayScore);

    LocalDateTime getStartTime();
}

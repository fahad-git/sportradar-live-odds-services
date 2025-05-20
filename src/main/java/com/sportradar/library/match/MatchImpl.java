package com.sportradar.library.match;

import com.sportradar.library.team.Team;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class MatchImpl implements Match {

    private final Team homeTeam;

    private final Team awayTeam;

    private final LocalDateTime startTime;

    public MatchImpl(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = LocalDateTime.now();
    }

    @Override
    public int getTotalScore() {
        return homeTeam.getScore() + awayTeam.getScore();
    }

    public void updateScore(int homeScore, int awayScore) {
        homeTeam.setScore(homeScore);
        awayTeam.setScore(awayScore);
    }
}

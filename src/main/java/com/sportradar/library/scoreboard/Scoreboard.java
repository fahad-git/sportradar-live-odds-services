package com.sportradar.library.scoreboard;

import com.sportradar.library.match.MatchSummary;

import java.util.List;

public interface Scoreboard {

    void startMatch(String homeTeamName, String awayTeamName);

    void finishMatch(String homeTeamName, String awayTeamName);

    void updateScore(String homeTeamName, String awayTeamName, int homeTeamScore, int awayTeamScore);

    List<MatchSummary> getOrderedMatches();
}

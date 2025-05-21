package com.sportradar.library.match;

public record MatchSummary(
        String homeTeamName,
        int homeTeamScore,
        String awayTeamName,
        int awayTeamScore
) {}

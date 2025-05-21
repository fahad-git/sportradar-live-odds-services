package com.sportradar.library.scoreboard;

import com.sportradar.library.exceptions.DuplicateMatchException;
import com.sportradar.library.exceptions.MatchNotFoundException;
import com.sportradar.library.exceptions.TeamNameException;
import com.sportradar.library.match.Match;
import com.sportradar.library.match.MatchImpl;
import com.sportradar.library.match.MatchSummary;
import com.sportradar.library.team.Team;
import lombok.Synchronized;

import java.util.*;

public class ScoreBoardImpl implements Scoreboard {

    private final List<Match> matches = new ArrayList<>();

    @Synchronized
    @Override
    public void startMatch(String homeTeamName, String awayTeamName) {

        validateTeamNames(homeTeamName, awayTeamName);

        // Check if this match already exists
        if (findMatch(homeTeamName, awayTeamName).isPresent()) {
            throw new DuplicateMatchException(
                    "It is not possible to have two same matches at the same time."
            );
        }

        // Create new match and add it to active list
        MatchImpl match = new MatchImpl(new Team(homeTeamName), new Team(awayTeamName));
        matches.add(match);
    }

    @Synchronized
    @Override
    public void finishMatch(String homeTeamName, String awayTeamName) {

        validateTeamNames(homeTeamName, awayTeamName);

        // Remove match by team names
        boolean removed = matches.removeIf(match ->
                match.getHomeTeam().getName().equalsIgnoreCase(homeTeamName) &&
                        match.getAwayTeam().getName().equalsIgnoreCase(awayTeamName)
        );

        if (!removed) {
            throw new MatchNotFoundException("Cannot finish match that doesn't exist.");
        }
    }

    @Synchronized
    @Override
    public void updateScore(String homeTeamName, String awayTeamName, int homeTeamScore, int awayTeamScore) {

        validateTeamScores(homeTeamScore, awayTeamScore);

        Optional<Match> optionalMatch = findMatch(homeTeamName, awayTeamName);

        // If match not found, throw exception
        if (optionalMatch.isEmpty()) {
            throw new MatchNotFoundException("It is not possible to update match which is not active.");
        }

        // Update score if found
        Match foundMatch = optionalMatch.get();
        foundMatch.updateScore(homeTeamScore, awayTeamScore);
    }

    @Override
    public List<MatchSummary> getOrderedMatches() {
        return matches.stream()
                .sorted(Comparator.comparing(this::getTotalScore, Comparator.reverseOrder())
                        .thenComparing(Match::getStartTime, Comparator.reverseOrder()))
                .map(match -> new MatchSummary(
                        match.getHomeTeam().getName(),
                        match.getHomeTeam().getScore(),
                        match.getAwayTeam().getName(),
                        match.getAwayTeam().getScore()))
                .toList();
    }


    private Optional<Match> findMatch(String homeTeamName, String awayTeamName) {
        // Utility method to find a match by team names
        return matches.stream()
                .filter(m -> m.getHomeTeam().getName().equalsIgnoreCase(homeTeamName))
                .filter(m -> m.getAwayTeam().getName().equalsIgnoreCase(awayTeamName))
                .findFirst();
    }

    private int getTotalScore(Match match) {
        // Calculates total score of a given match
        return match.getHomeTeam().getScore() + match.getAwayTeam().getScore();
    }

    private void validateTeamScores(int homeTeamScore, int awayTeamScore) {
        // Validates that scores are not negative
        if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new IllegalArgumentException(
                    "It is not possible to update match with negative values.");
        }
    }

    private void validateTeamNames(String homeTeam, String awayTeam) {
        // Validates team names are non-null, non-blank, and not the same
        if (homeTeam == null || awayTeam == null || homeTeam.isBlank() || awayTeam.isBlank()) {
            throw new TeamNameException("Team name cannot be empty.");
        }
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new TeamNameException("Teams cannot have the same name.");
        }
    }

}

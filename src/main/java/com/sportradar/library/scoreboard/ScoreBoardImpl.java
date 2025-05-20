package com.sportradar.library.scoreboard;

import com.sportradar.library.exceptions.ScoreBoardException;
import com.sportradar.library.exceptions.TeamNameException;
import com.sportradar.library.match.Match;
import com.sportradar.library.match.MatchImpl;
import com.sportradar.library.team.Team;

import java.util.*;

public class ScoreBoardImpl implements Scoreboard {

    private final List<Match> matches = new ArrayList<>();

    @Override
    public void startMatch(String homeTeamName, String awayTeamName) {

        validateTeamNames(homeTeamName, awayTeamName);

        // Check if this match already exists
        if(findMatch(homeTeamName, awayTeamName).isPresent()) {
            throw new ScoreBoardException(
                    "It is not possible to have two same matches at the same time."
            );
        }

        // Create new match and add it to active list
        MatchImpl Match = new MatchImpl(new Team(homeTeamName), new Team(awayTeamName));
        matches.add(Match);
    }

    @Override
    public void finishMatch(String homeTeamName, String awayTeamName) {
        // Remove match by team names
        matches.removeIf(match ->
                match.getHomeTeam().getName().equalsIgnoreCase(homeTeamName) &&
                        match.getAwayTeam().getName().equalsIgnoreCase(awayTeamName)
        );
    }

    @Override
    public void updateScore(String homeTeamName, String awayTeamName, int homeTeamScore, int awayTeamScore) {

        validateTeamScores(homeTeamScore, awayTeamScore);

        Optional<Match> optionalMatch = findMatch(homeTeamName, awayTeamName);

        // If match not found, throw exception
        if (optionalMatch.isEmpty()) {
            throw new ScoreBoardException("It is not possible to update match which is not active.");
        }

        // Update score if found
        Match foundMatch = optionalMatch.get();
        foundMatch.updateScore(homeTeamScore, awayTeamScore);
    }

    @Override
    public List<Match> getOrderedMatches() {
        // Return unmodifiable sorted list:
        // - First by total score descending
        // - Then by start time descending (i.e., more recent first)
        return Collections.unmodifiableList(
                matches.stream()
                        .sorted(Comparator.comparing(Match::getTotalScore)
                                .thenComparing(Match::getStartTime)
                                .reversed())
                        .toList()
        );
    }

    // Utility method to find a match by team names
    private Optional<Match> findMatch(String homeTeamName, String awayTeamName){
        return matches.stream()
                .filter(m -> m.getHomeTeam().getName().equalsIgnoreCase(homeTeamName))
                .filter(m -> m.getAwayTeam().getName().equalsIgnoreCase(awayTeamName))
                .findFirst();
    }

    // Validates that scores are not negative
    private void validateTeamScores(int homeTeamScore, int awayTeamScore) {
        if(homeTeamScore < 0 || awayTeamScore < 0 ) {
            throw new IllegalArgumentException(
                    "It is not possible to update match with negative values.");
        }
    }

    // Validates team names are non-null, non-blank, and not the same
    private void validateTeamNames(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.isBlank() || awayTeam.isBlank()) {
            throw new TeamNameException("Team name cannot be empty.");
        }
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new TeamNameException("Teams cannot have the same name.");
        }
    }
}

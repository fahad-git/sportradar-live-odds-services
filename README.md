# Sportradar Live Football World Cup Scoreboard

This project is a **Live Football World Cup Scoreboard Library** that helps manage and display live football matches and their scores. It allows you to start new matches, update scores, finish matches, and get a summary of the ongoing games, sorted by their total scores and start times.

---

##  Features

- **Live Scoreboard**: Track all ongoing matches with up-to-date scores.
- **Start Match**: Create a new match with initial score 0–0.
- **Update Score**: Update the score for any ongoing match.
- **Finish Match**: Mark a match as finished and remove it from the scoreboard.
- **Match Summary**: Retrieve an ordered list of ongoing matches sorted by:
  - Total score (descending)
  - Start time (most recent first)

---

## Getting Started

### Create an Instance

```java
Scoreboard scoreboard = new ScoreBoardImpl();
```

---

### Start a New Match

```java
scoreboard.startMatch("Spain", "Brazil");
```

  Automatically starts the match with a score of 0-0.

  Throws:
- `TeamNameException` if the team names are blank, null, or identical.
- `DuplicateMatchException` if the match already exists.

---

### Update Score

```java
scoreboard.updateScore("Spain", "Brazil", 2, 3);
```

  Throws:
- `MatchNotFoundException` if match is not found.
- `InvalidScoreException` if any score is negative.

---

### Finish Match

```java
scoreboard.finishMatch("Spain", "Brazil");
```

  Throws:
- `MatchNotFoundException` if the match does not exist.

---

### Get Summary of Ongoing Matches

```java
List<Match> summary = scoreboard.getOrderedMatches();
for (Match match : summary) {
    System.out.printf("%s %d - %s %d%n",
        match.getHomeTeam().getName(),
        match.getHomeTeam().getScore(),
        match.getAwayTeam().getName(),
        match.getAwayTeam().getScore()
    );
}
```

🔁 Sorted by:
1. Total score (descending)
2. Start time (most recent first)

---

##   Assumptions

- Team names are **case-insensitive**.
- A match cannot be started with:
  - The same team on both sides
  - Empty or blank team names
- You cannot start **duplicate matches**.
- Scores must be **non-negative integers**.
- Finished matches are removed from the scoreboard.

---

##   Running Tests

This project uses **JUnit**.

To run the unit tests:

1. **From IntelliJ**: Follow [JetBrains guide](https://www.jetbrains.com/help/idea/work-with-tests-in-maven.html#debug_maven).
2. **Using Maven**:
    ```bash
    mvn test
    ```

All core functionalities are covered under `ScoreBoardImplTest`, including edge cases and error scenarios.

---

##   Suggestions or Feedback

Feel free to open an issue or submit a pull request if you have any feedback or suggestions for improvement.

---

© 2025 Sportradar Candidate Submission

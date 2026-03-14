package edu.nd.pmcburne.hwapp.one.mappers

import edu.nd.pmcburne.hwapp.one.data.database.GameEntity
import edu.nd.pmcburne.hwapp.one.model.Game
import edu.nd.pmcburne.hwapp.one.model.GameStatus
import edu.nd.pmcburne.hwapp.one.network.ApiGame

fun ApiGame.toEntity(
    dateKey: String,
    gender: String
): GameEntity {
    val mappedStatus = when (gameState.lowercase()) {
        "pre" -> GameStatus.UPCOMING
        "live" -> GameStatus.LIVE
        "final" -> GameStatus.FINAL
        else -> GameStatus.UPCOMING
    }

    return GameEntity(
        gameId = gameID,
        dateKey = dateKey,
        gender = gender,
        homeTeam = home.names.short.ifBlank { "Home Team" },
        awayTeam = away.names.short.ifBlank { "Away Team" },
        homeScore = home.score,
        awayScore = away.score,
        status = mappedStatus.name,
        startTime = startTime,
        currentPeriod = currentPeriod,
        clock = contestClock,
        homeWinner = home.winner,
        awayWinner = away.winner
    )
}

fun GameEntity.toDomain(): Game {
    return Game(
        gameId = gameId,
        dateKey = dateKey,
        gender = gender,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        homeScore = homeScore,
        awayScore = awayScore,
        status = GameStatus.valueOf(status),
        startTime = startTime,
        currentPeriod = currentPeriod,
        clock = clock,
        homeWinner = homeWinner,
        awayWinner = awayWinner
    )
}
package edu.nd.pmcburne.hwapp.one.data.mappers

import edu.nd.pmcburne.hwapp.one.data.database.GameEntity
import edu.nd.pmcburne.hwapp.one.model.Game

fun GameEntity.toGame(): Game {
    return Game(
        id = id,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        homeScore = homeScore,
        awayScore = awayScore,
        status = status,
        period = period,
        clock = clock
    )
}

fun Game.toEntity(date: String, gender: String): GameEntity {
    return GameEntity(
        id = id,
        date = date,
        gender = gender,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        homeScore = homeScore,
        awayScore = awayScore,
        status = status,
        period = period,
        clock = clock
    )
}
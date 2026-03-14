package edu.nd.pmcburne.hwapp.one.network

/***************************************************************************************
 * REFERENCES
 * URL: https://square.github.io/retrofit/
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/

import retrofit2.http.GET
import retrofit2.http.Path

data class ScoreboardResponse(
    val games: List<GameWrapper> = emptyList()
)

data class GameWrapper(
    val game: ApiGame
)

data class ApiGame(
    val gameID: String = "",
    val gameState: String = "",
    val startTime: String = "",
    val currentPeriod: String = "",
    val contestClock: String = "",
    val finalMessage: String = "",
    val home: ApiTeam = ApiTeam(),
    val away: ApiTeam = ApiTeam()
)

data class ApiTeam(
    val score: String = "",
    val winner: Boolean = false,
    val names: ApiNames = ApiNames()
)

data class ApiNames(
    val short: String = ""
)

interface BasketballApiService {
    @GET("scoreboard/basketball-{gender}/d1/{year}/{month}/{day}")
    suspend fun getScoreboard(
        @Path("gender") gender: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String
    ): ScoreboardResponse
}
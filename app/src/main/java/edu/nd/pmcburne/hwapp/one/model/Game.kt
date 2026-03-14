package edu.nd.pmcburne.hwapp.one.model

enum class Gender(val apiValue: String, val label: String) {
    MEN("men", "Men"),
    WOMEN("women", "Women")
}

enum class GameStatus {
    UPCOMING,
    LIVE,
    FINAL
}

/***************************************************************************************
 * REFERENCES
 * URL: https://kotlinlang.org/docs/properties.html#custom-getters-and-setters
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/

data class Game(
    val gameId: String,
    val dateKey: String,
    val gender: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: String,
    val awayScore: String,
    val status: GameStatus,
    val startTime: String,
    val currentPeriod: String,
    val clock: String,
    val homeWinner: Boolean,
    val awayWinner: Boolean
) {
    val statusLine: String
        get() {
            val period = when {
                currentPeriod.equals("HALFTIME", ignoreCase = true) -> "Halftime"
                currentPeriod.isNotBlank() -> currentPeriod
                else -> "Live"
            }
            return when (status) {
                GameStatus.UPCOMING -> startTime.ifBlank { "Upcoming" }
                GameStatus.LIVE ->
                    if (clock.isNotBlank() && clock != "0:00" && clock != "00:00") "$period - $clock"
                    else period
                GameStatus.FINAL -> "Final"
            }
        }

    val winnerLabel: String
        get() = when {
            homeWinner -> "$homeTeam won"
            awayWinner -> "$awayTeam won"
            else -> ""
        }
}
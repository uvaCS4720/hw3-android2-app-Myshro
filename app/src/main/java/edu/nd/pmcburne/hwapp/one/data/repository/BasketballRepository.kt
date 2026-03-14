package edu.nd.pmcburne.hwapp.one.data.repository

import edu.nd.pmcburne.hwapp.one.data.database.GameDao
import edu.nd.pmcburne.hwapp.one.model.Game
import edu.nd.pmcburne.hwapp.one.network.BasketballApiService

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#1
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/


class BasketballRepository(
    private val api: BasketballApiService,
    private val dao: GameDao
) {

    suspend fun getGames(date: String, gender: String): List<Game> {

        try {
            val apiGames = api.getGames(gender, "2026", "02", "17")
            // online impl.

        } catch (e: Exception) {
            // offline fallback
        }

        return dao.getGames(date, gender)
    }
}
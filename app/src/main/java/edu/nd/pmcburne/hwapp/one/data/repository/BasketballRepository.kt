package edu.nd.pmcburne.hwapp.one.data.repository

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import edu.nd.pmcburne.hwapp.one.data.database.GameDao
import edu.nd.pmcburne.hwapp.one.mappers.toDomain
import edu.nd.pmcburne.hwapp.one.mappers.toEntity
import edu.nd.pmcburne.hwapp.one.model.Game
import edu.nd.pmcburne.hwapp.one.network.BasketballApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#1
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/


class BasketballRepository(
    private val context: Context,
    private val api: BasketballApiService,
    private val gameDao: GameDao
) {
    fun observeGames(date: LocalDate, gender: String): Flow<List<Game>> {
        val dateKey = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        return gameDao.observeGames(dateKey, gender).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    suspend fun refresh(date: LocalDate, gender: String): Result<Unit> {
        val dateKey = date.format(DateTimeFormatter.ISO_LOCAL_DATE)

        if (!isOnline()) {
            return Result.failure(IllegalStateException("No internet connection. Showing saved scores if available."))
        }

        return try {
            val year = date.year.toString()
            val month = "%02d".format(date.monthValue)
            val day = "%02d".format(date.dayOfMonth)

            val response = api.getScoreboard(
                gender = gender,
                year = year,
                month = month,
                day = day
            )

            val entities = response.games.map { wrapper ->
                wrapper.game.toEntity(
                    dateKey = dateKey,
                    gender = gender
                )
            }

            gameDao.deleteForDateAndGender(dateKey, gender)
            gameDao.insertAll(entities)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
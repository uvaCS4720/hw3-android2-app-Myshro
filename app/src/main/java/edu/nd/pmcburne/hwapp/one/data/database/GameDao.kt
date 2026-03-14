package edu.nd.pmcburne.hwapp.one.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.nd.pmcburne.hwapp.one.model.Game
import kotlinx.coroutines.flow.Flow

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/training/data-storage/room/accessing-data
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/


@Dao
interface GameDao {
    @Query("SELECT * FROM games WHERE dateKey = :dateKey AND gender = :gender ORDER BY status, startTime, awayTeam")
    fun observeGames(dateKey: String, gender: String): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Query("DELETE FROM games WHERE dateKey = :dateKey AND gender = :gender")
    suspend fun deleteForDateAndGender(dateKey: String, gender: String)
}
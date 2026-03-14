package edu.nd.pmcburne.hwapp.one.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.nd.pmcburne.hwapp.one.model.Game

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/training/data-storage/room/accessing-data
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/


@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<Game>)

    @Query("SELECT * FROM games WHERE date = :date AND gender = :gender")
    suspend fun getGames(date: String, gender: String): List<Game>
}
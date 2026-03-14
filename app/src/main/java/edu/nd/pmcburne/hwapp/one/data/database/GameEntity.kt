package edu.nd.pmcburne.hwapp.one.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/training/data-storage/room/defining-data
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/


@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: String,
    val date: String,
    val gender: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val status: String,
    val period: String?,
    val clock: String?
)
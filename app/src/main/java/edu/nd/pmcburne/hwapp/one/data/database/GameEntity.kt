package edu.nd.pmcburne.hwapp.one.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/training/data-storage/room/defining-data
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/
@Entity(
    tableName = "games",
    primaryKeys = ["gameId", "dateKey", "gender"]
)
data class GameEntity(
    val gameId: String,
    val dateKey: String,   // yyyy-MM-dd
    val gender: String,    // men / women

    val homeTeam: String,
    val awayTeam: String,

    val homeScore: String,
    val awayScore: String,

    val status: String,    // UPCOMING / LIVE / FINAL
    val startTime: String,
    val currentPeriod: String,
    val clock: String,

    val homeWinner: Boolean,
    val awayWinner: Boolean
)
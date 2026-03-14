package edu.nd.pmcburne.hwapp.one.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/training/data-storage/room
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/

@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
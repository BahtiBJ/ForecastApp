package com.bbj.myapplication.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(entities = [DatabaseModel::class],version = 4,exportSchema = false)
abstract class ForecastDataBase : RoomDatabase() {
    abstract fun getDAO() : DatabaseDAO


}
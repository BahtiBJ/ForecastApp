package com.bbj.myapplication.data.database

import androidx.room.*

@Dao
interface DatabaseDAO {

    @Query("Select * from DatabaseModel where date = :date")
    suspend fun getByDate(date : String) : List<DatabaseModel>

    @Query("Select * from DatabaseModel")
    suspend fun getAll() : List<DatabaseModel>

    @Query("Delete from DatabaseModel Where date = :date")
    suspend fun delete(date : String)

    @Query("Delete from DatabaseModel")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(model : DatabaseModel)

    @Transaction
    suspend fun insertWithRefresh(model: DatabaseModel){
        deleteAll()
        insert(model)
    }

}
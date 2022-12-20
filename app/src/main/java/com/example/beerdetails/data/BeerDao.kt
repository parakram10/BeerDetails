package com.example.beerdetails.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BeerDao {
    @Query("Select * from $TABLE_NAME")
    fun getAllData(): List<BeerEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(beerEntity: BeerEntity)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    fun deleteByUserId(id: Int)
}
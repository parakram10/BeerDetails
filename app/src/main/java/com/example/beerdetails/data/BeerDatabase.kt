package com.example.beerdetails.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BeerEntity::class], version = 1)
abstract class BeerDatabase : RoomDatabase() {

    abstract fun beerDao(): BeerDao

    companion object {
        private var INSTANCE: BeerDatabase? = null

        fun getDatabase(context: Context): BeerDatabase? {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context, BeerDatabase::class.java, DATABASE_NAME)
                        .allowMainThreadQueries().build()
            }
            return INSTANCE
        }
    }
}
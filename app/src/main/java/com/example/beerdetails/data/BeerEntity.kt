package com.example.beerdetails.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME = "beer_table"
const val DATABASE_NAME = "beer_database"

@Entity(tableName = TABLE_NAME)
data class BeerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int = -1,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "tagline") val tagline: String? = null,
    @ColumnInfo(name = "first_brewed") val firstBrewed: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    @ColumnInfo(name = "abv") val abv: Float = -1F,
    @ColumnInfo(name = "brewers_tips") val brewersTips: String? = null
)
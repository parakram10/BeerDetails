package com.example.beerdetails.data

import retrofit2.Response

interface BeerRepo {
    suspend fun getBeerData(): Response<List<BeersData>>

    suspend fun getRandomBeer(): Response<List<BeersData>>

    suspend fun getFavouriteBeerData() : List<BeerEntity>?

    suspend fun clearFavouriteBeerData()

    suspend fun addFavouriteBeerInDb(beersData: BeerEntity)

    suspend fun removeFavouriteBeerInDb(id: Int)
}
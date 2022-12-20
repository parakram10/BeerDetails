package com.example.beerdetails.data

import retrofit2.Response
import retrofit2.http.GET

interface BeersApiService {
    @GET("beers")
    suspend fun getAllBeers(): Response<List<BeersData>>

    @GET("beers/random")
    suspend fun getRandomBeer(): Response<List<BeersData>>
}
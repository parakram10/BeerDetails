package com.example.beerdetails.domain

import com.example.beerdetails.data.*

class BeerRepoImpl(
    private val apiService: BeersApiService, private val beerDatabase: BeerDatabase?
) : BeerRepo {

    override suspend fun getBeerData() = apiService.getAllBeers()

    override suspend fun getRandomBeer() = apiService.getRandomBeer()

    override suspend fun getFavouriteBeerData(): List<BeerEntity>? =
        beerDatabase?.beerDao()?.getAllData()

    override suspend fun clearFavouriteBeerData() {
        beerDatabase?.beerDao()?.deleteAll()
    }

    override suspend fun addFavouriteBeerInDb(beersData: BeerEntity) {
        beerDatabase?.beerDao()?.insert(beersData)
    }

    override suspend fun removeFavouriteBeerInDb(id: Int) {
        beerDatabase?.beerDao()?.deleteByUserId(id)
    }
}
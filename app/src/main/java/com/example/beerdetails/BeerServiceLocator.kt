package com.example.beerdetails

import android.content.Context
import com.example.beerdetails.data.BeerDatabase
import com.example.beerdetails.data.BeerRepo
import com.example.beerdetails.data.BeersApiService
import com.example.beerdetails.domain.BeerRepoImpl
import com.example.beerdetails.domain.RetrofitHelper

object BeerServiceLocator {
    private var repo: BeerRepo? = null

    fun getRepo(context: Context): BeerRepo {
        if (repo == null) {
            val apiService: BeersApiService =
                RetrofitHelper.getInstance().create(BeersApiService::class.java)

            val database = BeerDatabase.getDatabase(context)
            repo = BeerRepoImpl(apiService, database)
        }
        return repo as BeerRepo
    }
}
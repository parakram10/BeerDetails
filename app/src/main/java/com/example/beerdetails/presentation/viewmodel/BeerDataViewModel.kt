package com.example.beerdetails.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.beerdetails.data.BeerEntity
import com.example.beerdetails.data.BeerRepo
import com.example.beerdetails.data.BeersData
import kotlinx.coroutines.launch

class BeerDataViewModel(private val beerRepo: BeerRepo) : ViewModel() {

    val allBeerData: MutableLiveData<List<BeersData>> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData(true)

    val randomBeerData: MutableLiveData<List<BeersData>> = MutableLiveData()
    val randomErrorMessage = MutableLiveData<String>()
    val randomLoading = MutableLiveData(true)

    val favouriteBeerData: MutableLiveData<List<BeerEntity>> = MutableLiveData()
    val favouriteBeerLoading = MutableLiveData(true)

    val favouriteErrorMessage = MutableLiveData<String>()
    val clearFavouriteErrorMessage = MutableLiveData<String>()
    val saveFavouriteErrorMessage = MutableLiveData<String>()

    fun fetchBeerList() {
        viewModelScope.launch {
            val response = beerRepo.getBeerData()
            if (response.isSuccessful) {
                allBeerData.postValue(response.body())
            } else {
                errorMessage.value = response.message()
            }
            loading.value = false
        }
    }

    fun fetchRandomBeer() {
        viewModelScope.launch {
            val response = beerRepo.getRandomBeer()
            if (response.isSuccessful) {
                randomBeerData.postValue(response.body())
            } else {
                randomErrorMessage.value = response.message()
            }
            randomLoading.value = false
        }
    }

    fun saveFavouriteBeerInDb(beersData: BeerEntity?) {
        if (beersData == null) return
        viewModelScope.launch {
            try {
                beerRepo.addFavouriteBeerInDb(beersData)
            } catch (e: Exception) {
                saveFavouriteErrorMessage.value = e.message
            }
        }
    }

    fun fetchFavouriteBeerDataFromDb() {
        viewModelScope.launch {
            val data = beerRepo.getFavouriteBeerData()
            try {
                favouriteBeerData.postValue(data)
            } catch (e: Exception) {
                favouriteErrorMessage.value = e.message
            }
            favouriteBeerLoading.value = false
        }
    }

    fun removeAllDataFromDb() {
        viewModelScope.launch {
            try {
                beerRepo.clearFavouriteBeerData()
            } catch (e: Exception) {
                clearFavouriteErrorMessage.value = e.message
            }
        }
    }

    fun removeFavouriteBeerFromDb(id: Int) {
        viewModelScope.launch {
            try {
                beerRepo.removeFavouriteBeerInDb(id)
                fetchFavouriteBeerDataFromDb()
            } catch (e: Exception) {
                favouriteErrorMessage.value = e.message
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class BeerDataViewModelFactory constructor(private val beerRepo: BeerRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BeerDataViewModel::class.java)) {
            BeerDataViewModel(this.beerRepo) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
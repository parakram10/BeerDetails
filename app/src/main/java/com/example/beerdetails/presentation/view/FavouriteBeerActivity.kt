package com.example.beerdetails.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.beerdetails.MainApplication
import com.example.beerdetails.data.BeerDatabase
import com.example.beerdetails.data.BeersApiService
import com.example.beerdetails.data.BeersData
import com.example.beerdetails.databinding.ActivityFavouriteBeerBinding
import com.example.beerdetails.domain.BeerRepoImpl
import com.example.beerdetails.domain.RetrofitHelper
import com.example.beerdetails.presentation.BeerAdapter
import com.example.beerdetails.presentation.FavouriteBeerAdapter
import com.example.beerdetails.presentation.viewmodel.BeerDataViewModel
import com.example.beerdetails.presentation.viewmodel.BeerDataViewModelFactory

class FavouriteBeerActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavouriteBeerBinding
    private lateinit var viewModel: BeerDataViewModel
    private var adapter: FavouriteBeerAdapter = FavouriteBeerAdapter(this)

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(getIntent(context))
        }

        fun getIntent(context: Context) = Intent(context, FavouriteBeerActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBeerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService: BeersApiService =
            RetrofitHelper.getInstance().create(BeersApiService::class.java)

        val database = BeerDatabase.getDatabase(applicationContext)

        viewModel = ViewModelProvider(
            this, BeerDataViewModelFactory(BeerRepoImpl(apiService, database))
        )[BeerDataViewModel::class.java]

        binding.rvBears.adapter = adapter

        setUpObserver()
        viewModel.fetchFavouriteBeerDataFromDb()
    }

    private fun setUpObserver() {
        viewModel.favouriteBeerData.observe(this) {
            if (it.isEmpty()) {
                binding.apply {
                    rvBears.visibility = View.GONE
                    ivBroken.visibility = View.VISIBLE
                    tvText.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                }
            } else {
                binding.apply {
                    rvBears.visibility = View.VISIBLE
                    ivBroken.visibility = View.GONE
                    tvText.visibility = View.GONE
                    progress.visibility = View.GONE
                }
                adapter.setData(it)
            }
        }

        viewModel.favouriteBeerLoading.observe(this){
            binding.apply {
                progress.visibility = View.VISIBLE
                rvBears.visibility = View.GONE
                ivBroken.visibility = View.GONE
                tvText.visibility = View.GONE
            }
        }

        viewModel.favouriteErrorMessage.observe(this){
            binding.apply {
                progress.visibility = View.GONE
                rvBears.visibility = View.GONE
                ivBroken.visibility = View.VISIBLE
                tvText.visibility = View.VISIBLE
            }
        }
    }
}
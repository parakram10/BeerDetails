package com.example.beerdetails.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.beerdetails.BeerServiceLocator
import com.example.beerdetails.data.BeersData
import com.example.beerdetails.databinding.ActivityDashboardBinding
import com.example.beerdetails.presentation.BeerAdapter
import com.example.beerdetails.presentation.viewmodel.BeerDataViewModel
import com.example.beerdetails.presentation.viewmodel.BeerDataViewModelFactory

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: BeerDataViewModel
    private var beerList: List<BeersData> = emptyList()
    private var requiredList: List<BeersData> = emptyList()
    private var adapter: BeerAdapter = BeerAdapter(this)

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(getIntent(context))
        }

        fun getIntent(context: Context) = Intent(context, DashboardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, BeerDataViewModelFactory(BeerServiceLocator.getRepo(this))
        )[BeerDataViewModel::class.java]
        binding.rvBears.adapter = adapter
        binding.filterView.tvAll.isChecked = true

        setUpObserver()
        setUpListener()
        viewModel.fetchBeerList()
    }

    private fun setUpListener() {
        binding.filterView.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                binding.filterView.tvAll.id -> {
                    requiredList = beerList
                    if (requiredList.isEmpty()) {
                        binding.noBeerFound.visibility = View.VISIBLE
                        binding.rvBears.visibility = View.GONE
                    } else {
                        binding.noBeerFound.visibility = View.GONE
                        binding.rvBears.visibility = View.VISIBLE
                        adapter.setData(requiredList)
                    }
                }
                binding.filterView.tvLight.id -> {
                    requiredList = beerList.filter { it.abv < 6 }
                    if (requiredList.isEmpty()) {
                        binding.noBeerFound.visibility = View.VISIBLE
                        binding.rvBears.visibility = View.GONE
                    } else {
                        binding.noBeerFound.visibility = View.GONE
                        binding.rvBears.visibility = View.VISIBLE
                        adapter.setData(requiredList)
                    }
                }
                binding.filterView.tvMedium.id -> {
                    requiredList = beerList.filter { it.abv >= 6 && it.abv < 10 }
                    if (requiredList.isEmpty()) {
                        binding.noBeerFound.visibility = View.VISIBLE
                        binding.rvBears.visibility = View.GONE
                    } else {
                        binding.noBeerFound.visibility = View.GONE
                        binding.rvBears.visibility = View.VISIBLE
                        adapter.setData(requiredList)
                    }
                }
                binding.filterView.tvHard.id -> {
                    requiredList = beerList.filter { it.abv >= 10 }
                    if (requiredList.isEmpty()) {
                        binding.noBeerFound.visibility = View.VISIBLE
                        binding.rvBears.visibility = View.GONE
                    } else {
                        binding.noBeerFound.visibility = View.GONE
                        binding.rvBears.visibility = View.VISIBLE
                        adapter.setData(requiredList)
                    }
                }
            }
        }

        binding.randomCard.root.setOnClickListener { RandomBeerActivity.startActivity(this) }

        binding.ivHeart.setOnClickListener { FavouriteBeerActivity.startActivity(this) }
    }

    private fun setUpObserver() {
        viewModel.allBeerData.observe(this) {
            binding.apply {
                view2.visibility = View.GONE
                view3.visibility = View.GONE
                view4.visibility = View.GONE
                view5.visibility = View.GONE
                rvBears.visibility = View.VISIBLE
                tvBeers.visibility = View.VISIBLE
                filterView.root.visibility = View.VISIBLE
            }

            beerList = it
            requiredList = beerList
            adapter.setData(requiredList)
        }

        viewModel.errorMessage.observe(this) {

        }

        viewModel.loading.observe(this) {
            binding.apply {
                view2.visibility = View.VISIBLE
                view3.visibility = View.VISIBLE
                view4.visibility = View.VISIBLE
                view5.visibility = View.VISIBLE
                rvBears.visibility = View.GONE
                tvBeers.visibility = View.VISIBLE
                filterView.root.visibility = View.VISIBLE
            }
        }
    }
}
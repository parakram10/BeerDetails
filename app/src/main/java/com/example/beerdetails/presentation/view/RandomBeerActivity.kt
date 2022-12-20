package com.example.beerdetails.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.beerdetails.BeerServiceLocator
import com.example.beerdetails.R
import com.example.beerdetails.data.BeerEntity
import com.example.beerdetails.data.BeerPreference
import com.example.beerdetails.databinding.ActivityRandomBeerBinding
import com.example.beerdetails.presentation.viewmodel.BeerDataViewModel
import com.example.beerdetails.presentation.viewmodel.BeerDataViewModelFactory

class RandomBeerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRandomBeerBinding
    private lateinit var viewModel: BeerDataViewModel

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(getIntent(context))
        }

        fun getIntent(context: Context) = Intent(context, RandomBeerActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomBeerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, BeerDataViewModelFactory(BeerServiceLocator.getRepo(this))
        )[BeerDataViewModel::class.java]

        setUpObserver()
        viewModel.fetchRandomBeer()
    }

    private fun setUpObserver() {
        viewModel.randomBeerData.observe(this) { beerData ->
            binding.successGroup.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE

            val favouriteIdList = BeerPreference(this).getFavouriteBeerIdList()
            val beerId = beerData?.get(0)?.id ?: -1
            val beerName = beerData?.get(0)?.name
            val beerTag = beerData?.get(0)?.tagline
            val description = beerData?.get(0)?.description
            val date = beerData?.get(0)?.firstBrewed
            val abv = beerData?.get(0)?.abv ?: 0F
            val malt = beerData?.get(0)?.ingredients?.malt
            val foodPairing = beerData?.get(0)?.foodPairing
            val beerImage = beerData?.get(0)?.imageUrl
            val brewerTip = beerData?.get(0)?.brewersTips

            binding.apply {
                Glide.with(this@RandomBeerActivity).load(beerImage).into(ivBeer)

                tvName.text = beerName
                tvDescriptionText.text = description
                tvBrewedAt.text = getString(R.string.first_brewed, date)

                var ingredientString = ""
                malt?.forEachIndexed { index, it ->
                    ingredientString += "${it.name} (${it.amount?.value} ${it.amount?.unit}) "
                    if (index != malt.size - 1) ingredientString += ", "
                }

                tvIngredientsText.text = ingredientString

                var foodPairingString = ""
                foodPairing?.forEach { foodPairingString += "$it, " }

                tvFoodPairingText.text = foodPairingString
                tvTipsText.text = brewerTip

                if (favouriteIdList?.contains(beerId.toString()) == true) {
                    binding.ivFavourite.setImageDrawable(getDrawable(R.drawable.heart_selected))
                } else {
                    binding.ivFavourite.setImageDrawable(getDrawable(R.drawable.heart_unselected))
                }

                ivBack.setOnClickListener { finish() }
                ivFavourite.setOnClickListener {
                    val favouriteList =
                        BeerPreference(this@RandomBeerActivity).getFavouriteBeerIdList()
                    if (favouriteList?.contains(beerId.toString()) == true) {
                        BeerPreference(this@RandomBeerActivity).removeFavouriteBeerId(beerId)
                        binding.ivFavourite.setImageDrawable(getDrawable(R.drawable.heart_unselected))
                        viewModel.removeFavouriteBeerFromDb(beerId)
                    } else {
                        BeerPreference(this@RandomBeerActivity).addFavouriteBeerId(beerId)
                        binding.ivFavourite.setImageDrawable(getDrawable(R.drawable.heart_selected))
                        viewModel.saveFavouriteBeerInDb(
                            BeerEntity(
                                id = beerId,
                                name = beerName,
                                tagline = beerTag,
                                firstBrewed = date,
                                description = description,
                                imageUrl = beerImage,
                                abv = abv,
                                brewersTips = brewerTip
                            )
                        )
                    }
                }
            }
        }

        viewModel.randomLoading.observe(this) {
            binding.successGroup.visibility = View.GONE
            binding.shimmer.visibility = View.VISIBLE
        }

        viewModel.randomErrorMessage.observe(this) {

        }
    }
}
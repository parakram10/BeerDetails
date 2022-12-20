package com.example.beerdetails.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.beerdetails.BeerServiceLocator
import com.example.beerdetails.R
import com.example.beerdetails.data.BeerEntity
import com.example.beerdetails.data.BeerPreference
import com.example.beerdetails.data.BeersData
import com.example.beerdetails.databinding.ActivityBeerDescriptionScreenBinding
import com.example.beerdetails.presentation.viewmodel.BeerDataViewModel
import com.example.beerdetails.presentation.viewmodel.BeerDataViewModelFactory

class BeerDescriptionScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBeerDescriptionScreenBinding
    private var beerData: BeersData? = null
    private lateinit var viewModel: BeerDataViewModel

    companion object {
        const val KEY_BEER_DATA = "beer_data"

        fun startActivity(context: Context, beerData: BeersData) {
            context.startActivity(getIntent(context, beerData))
        }

        fun getIntent(context: Context, beerData: BeersData) =
            Intent(context, BeerDescriptionScreenActivity::class.java).apply {
                putExtra(KEY_BEER_DATA, beerData)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeerDescriptionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, BeerDataViewModelFactory(BeerServiceLocator.getRepo(this))
        )[BeerDataViewModel::class.java]

        beerData = intent?.getParcelableExtra(KEY_BEER_DATA)
        if (beerData == null) {
            Toast.makeText(this, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val favouriteIdList = BeerPreference(this).getFavouriteBeerIdList()

        val beerId = beerData?.id ?: 0
        val beerName = beerData?.name
        val beerTag = beerData?.tagline
        val description = beerData?.description
        val date = beerData?.firstBrewed
        val malt = beerData?.ingredients?.malt
        val foodPairing = beerData?.foodPairing
        val beerImage = beerData?.imageUrl
        val brewerTip = beerData?.brewersTips
        val abv = beerData?.abv ?: 0F

        if (favouriteIdList?.contains(beerId.toString()) == true) {
            binding.ivFavourite.setImageDrawable(getDrawable(R.drawable.heart_selected))
        } else {
            binding.ivFavourite.setImageDrawable(getDrawable(R.drawable.heart_unselected))
        }

        binding.apply {
            Glide.with(this@BeerDescriptionScreenActivity).load(beerImage).into(ivBeer)

            tvName.text = beerName
            tvTagline.text = beerTag
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

            ivBack.setOnClickListener { finish() }
            ivFavourite.setOnClickListener {
                val favouriteList =
                    BeerPreference(this@BeerDescriptionScreenActivity).getFavouriteBeerIdList()
                if (favouriteList?.contains(beerId.toString()) == true) {
                    BeerPreference(this@BeerDescriptionScreenActivity).removeFavouriteBeerId(beerId)
                    binding.ivFavourite.setImageDrawable(getDrawable(R.drawable.heart_unselected))
                    viewModel.removeFavouriteBeerFromDb(beerId)
                } else {
                    BeerPreference(this@BeerDescriptionScreenActivity).addFavouriteBeerId(beerId)
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
}
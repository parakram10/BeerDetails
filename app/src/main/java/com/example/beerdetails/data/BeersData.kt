package com.example.beerdetails.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BeersData(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("name") val name: String? = null,
    @SerializedName("tagline") val tagline: String? = null,
    @SerializedName("first_brewed") val firstBrewed: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("abv") val abv: Float = -1F,
    @SerializedName("ingredients") val ingredients: Ingredients? = null,
    @SerializedName("food_pairing") val foodPairing: List<String>? = null,
    @SerializedName("brewers_tips") val brewersTips: String? = null
) : Parcelable {

    @Parcelize
    data class Ingredients(
        @SerializedName("malt") val malt: List<Malt>? = null,
        @SerializedName("yeast") val yeast: String? = null
    ) : Parcelable

    @Parcelize
    data class Malt(
        @SerializedName("name") val name: String? = null,
        @SerializedName("amount") val amount: MaltAmount? = null
    ) : Parcelable

    @Parcelize
    data class MaltAmount(
        @SerializedName("value") val value: Float = -1F,
        @SerializedName("unit") val unit: String? = null
    ) : Parcelable
}
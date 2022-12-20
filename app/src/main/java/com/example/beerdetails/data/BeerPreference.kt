package com.example.beerdetails.data

import android.content.Context
import android.content.SharedPreferences

class BeerPreference(context: Context) {
    companion object {
        private const val KEY_PREF = "beer_pref"
        private const val KEY_FAVOURITE_BEER_ID = "beer_id"
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun addFavouriteBeerId(id: Int) {
        val set: MutableSet<String> = mutableSetOf()
        set.add(id.toString())
        getFavouriteBeerIdList()?.let {
            set.addAll(it)
        }
        editor.putStringSet(KEY_FAVOURITE_BEER_ID, set)
        editor.commit()
    }

    fun removeFavouriteBeerId(id: Int) {
        val set: MutableSet<String> = mutableSetOf()
        getFavouriteBeerIdList()?.let {
            set.addAll(it)
        }
        set.remove(id.toString())
        editor.putStringSet(KEY_FAVOURITE_BEER_ID, set)
        editor.commit()
    }

    fun getFavouriteBeerIdList(): MutableSet<String>? =
        pref.getStringSet(KEY_FAVOURITE_BEER_ID, null)

}
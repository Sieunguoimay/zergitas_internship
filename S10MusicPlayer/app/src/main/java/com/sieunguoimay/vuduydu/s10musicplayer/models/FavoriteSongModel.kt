package com.sieunguoimay.vuduydu.s10musicplayer.models

import android.content.Context
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.FavouriteSong
import com.sieunguoimay.vuduydu.s10musicplayer.models.provider.FavouriteSongDatabaseHelper
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.FavouriteSongContract

class FavoriteSongModel(context: Context) : FavouriteSongContract.Model{
    private val database= FavouriteSongDatabaseHelper(context)

    override fun insertFavouriteSong(songId: Long, title:String) {
        database.insertFavouriteSong(FavouriteSong(songId, title))
    }

    override fun deleteFavouriteSong(songId: Long) {
        database.deleteBySongId(songId)
    }

}
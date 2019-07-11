package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity

import android.content.Context
import com.sieunguoimay.vuduydu.s10musicplayer.models.FavoriteSongModel
import com.sieunguoimay.vuduydu.yamp.data.model.Song

class FavouriteSongPresenter(context: Context):FavouriteSongContract.Presenter {
    private val model = FavoriteSongModel(context)
    var view: FavouriteSongContract.View? = null

    override fun like(song: Song) {
        model.insertFavouriteSong(song.id, song.title)
        view?.updateIconOnLike()
    }

    override fun unlike(song: Song) {
        model.deleteFavouriteSong(song.id)
        view?.updateIconOnUnlike()
    }
}
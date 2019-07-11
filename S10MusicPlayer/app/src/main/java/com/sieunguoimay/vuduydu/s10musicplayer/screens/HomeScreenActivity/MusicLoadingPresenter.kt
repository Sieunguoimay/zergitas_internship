package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity

import android.content.Context
import com.sieunguoimay.vuduydu.s10musicplayer.models.MusicLoadingModel
import com.sieunguoimay.vuduydu.s10musicplayer.models.tasks.LoadSongDataAsyncTask
import com.sieunguoimay.vuduydu.yamp.data.model.Song

class MusicLoadingPresenter(
    var context: Context,
    var view:MusicLoadingContract.View
) :MusicLoadingContract.Presenter, LoadSongDataAsyncTask.SongDataCallback{

    var model:MusicLoadingContract.Model

    init{
        model = MusicLoadingModel(context)

    }

    override fun loadSong(songList:ArrayList<Song>) {
        //assume that songs are loaded
        val songs = model.getSong(songList,this)

    }

    override fun onThumbailsLoaded() {
        view.onLoadedThumbails()
    }

    override fun onSongsLoaded(songList:ArrayList<Song>) {
        if(songList.size==0)
            view.showErrorMessage()
        else
            view.onLoadedSongList()
    }

}
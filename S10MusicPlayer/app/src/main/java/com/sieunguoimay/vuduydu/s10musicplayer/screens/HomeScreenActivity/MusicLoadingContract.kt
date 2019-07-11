package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity

import android.graphics.Bitmap
import com.sieunguoimay.vuduydu.s10musicplayer.models.tasks.LoadSongDataAsyncTask
import com.sieunguoimay.vuduydu.yamp.data.model.Song

//the model know nothing about the View
//as well as the presenter
//the presenter know them all
class MusicLoadingContract {
    interface View{
        fun onLoadedSongList()
        fun showErrorMessage()
        fun onLoadedThumbails()
//        fun displaySong()
    }
    interface Presenter{
        fun loadSong(songList:ArrayList<Song>)
    }
    interface Model{
        fun getSong(songList:ArrayList<Song>,callback: LoadSongDataAsyncTask.SongDataCallback)
    }
}
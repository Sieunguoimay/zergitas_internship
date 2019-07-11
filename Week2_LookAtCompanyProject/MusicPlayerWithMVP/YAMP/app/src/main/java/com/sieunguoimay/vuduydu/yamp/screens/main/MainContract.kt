package com.sieunguoimay.vuduydu.yamp.screens.main

import com.sieunguoimay.vuduydu.yamp.data.model.Song

class MainContract {
    interface View{
        fun showProgressBar()
        fun hideProgressBar()
        fun showSongs(songs: List<Song>)
        fun updateSongs(songs: Pair<Int,Song>)
        fun showErrorSongs()
    }
    interface Presenter{
        fun getSongs()
        fun setView(view: View)
    }
}
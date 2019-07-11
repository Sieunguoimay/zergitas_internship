package com.zergitas.demo.screens.main

import com.zergitas.demo.data.model.Song
import com.zergitas.demo.screens.BasePresenter

interface MainContract {
    /**
     * view
     */
    interface View {
        fun showProgressBar()

        fun hiddenProgressBar()

        fun showSongs(songs: List<Song>)

        fun updateSongs(pair:Pair<Int,Song>)

        fun showErrorSongs()

    }

    /**
     * presenter
     */
    interface Presenter : BasePresenter<View> {
        fun getSongs()
    }
}
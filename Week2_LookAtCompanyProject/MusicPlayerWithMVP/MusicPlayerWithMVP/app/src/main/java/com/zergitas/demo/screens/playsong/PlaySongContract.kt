package com.zergitas.demo.screens.playsong

import com.zergitas.demo.data.model.Song
import com.zergitas.demo.screens.BasePresenter

class PlaySongContract {

    /**
     * view
     */
    interface View {

        fun getSongs(): ArrayList<Song>

        fun getIndexCurrent(): Int

        fun isOpenFromNoti(): Boolean

        fun showLoopSetting(isLoop: Boolean)
    }

    /**
     * presenter
     */
    interface Presenter : BasePresenter<View> {

        fun onUpdateLoopSong()

        fun getLoopSong()
    }

}
package com.sieunguoimay.vuduydu.yamp.screens.playsong

import com.sieunguoimay.vuduydu.yamp.data.model.Song

class PlaySongContract {
    interface View{
        fun getSongs(): ArrayList<Song>
        fun getCurrentIndex():Int
        fun isOpenedFromNotification():Boolean
        fun displayLoopingStatus(isLoop:Boolean)
    }
}
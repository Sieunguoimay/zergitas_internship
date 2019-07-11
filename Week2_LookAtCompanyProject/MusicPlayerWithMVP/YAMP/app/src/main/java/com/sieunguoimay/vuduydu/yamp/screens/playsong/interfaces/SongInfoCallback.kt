package com.sieunguoimay.vuduydu.yamp.screens.playsong.interfaces

import android.graphics.Bitmap

interface SongInfoCallback<T> {
    fun updateSongInfo(data:T)

    fun updateSongState(status:Boolean)

    fun updateSongThumb(thumb: Bitmap?)
}
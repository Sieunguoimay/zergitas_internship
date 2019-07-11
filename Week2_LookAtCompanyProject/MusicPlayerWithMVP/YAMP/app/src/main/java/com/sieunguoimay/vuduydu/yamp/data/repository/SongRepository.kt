package com.sieunguoimay.vuduydu.yamp.data.repository

import android.content.Context
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import com.sieunguoimay.vuduydu.yamp.data.source.song.SongLocalDataContract
import com.sieunguoimay.vuduydu.yamp.data.source.song.SongLocalDataSource

class SongRepository(private val context:Context) {

    private var local:SongLocalDataSource

    init{
        local = SongLocalDataSource(context)
    }

    fun getSongs(callback: SongLocalDataContract.Callback<Song>){
        local.getSongs(callback)

    }



}

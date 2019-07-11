package com.zergitas.demo.data.repository

import android.content.Context
import com.zergitas.demo.data.model.Song
import com.zergitas.demo.data.source.SongDataSource
import com.zergitas.demo.data.source.local.song.SongLocalDataSource

class SongRepository(private val context: Context) : SongDataSource.LocalDataSource {

    //get data from local
    private var local: SongLocalDataSource

    //get data from remote

    init {
        local = SongLocalDataSource(context)
    }

    override fun getSongs(callback: SongDataSource.Callback<Song>) {
        local.getSongs(callback)
    }
}
package com.zergitas.demo.data.source.local.song

import android.content.Context
import com.zergitas.demo.data.model.Song
import com.zergitas.demo.data.source.SongDataSource

class SongLocalDataSource(private val context: Context)
    : SongDataSource.LocalDataSource {

    override fun getSongs(callback: SongDataSource.Callback<Song>) {
        GetSongsAsynctask(callback).execute(context)
    }
}
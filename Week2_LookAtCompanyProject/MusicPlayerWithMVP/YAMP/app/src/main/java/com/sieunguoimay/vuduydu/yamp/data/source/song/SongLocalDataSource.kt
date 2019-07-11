package com.sieunguoimay.vuduydu.yamp.data.source.song

import android.content.Context
import com.sieunguoimay.vuduydu.yamp.data.model.Song

class SongLocalDataSource(private val context: Context)
    : SongLocalDataContract.LocalDataSource{

    override fun getSongs(callback: SongLocalDataContract.Callback<Song>){
        GetSongsAsyncTask(callback).execute(context)
    }
}
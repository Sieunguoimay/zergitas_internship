package com.sieunguoimay.vuduydu.s10musicplayer.models

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.sieunguoimay.vuduydu.s10musicplayer.models.provider.LocalSongProvider
import com.sieunguoimay.vuduydu.s10musicplayer.models.tasks.LoadSongDataAsyncTask
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.MusicLoadingContract
import com.sieunguoimay.vuduydu.yamp.data.model.Song

class MusicLoadingModel(var context: Context):MusicLoadingContract.Model{
    override fun getSong(songList:ArrayList<Song>, callback: LoadSongDataAsyncTask.SongDataCallback){
        LoadSongDataAsyncTask(callback).execute(Pair(context, songList))
    }
}
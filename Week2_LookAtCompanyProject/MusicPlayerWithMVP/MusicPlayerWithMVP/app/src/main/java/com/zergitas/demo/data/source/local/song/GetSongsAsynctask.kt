package com.zergitas.demo.data.source.local.song

import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import com.zergitas.demo.R
import com.zergitas.demo.data.model.Song
import com.zergitas.demo.data.source.SongDataSource
import com.zergitas.demo.data.source.local.song.provider.ContentProvider
import com.zergitas.demo.utils.helper.scaleAndCropCenter

class GetSongsAsynctask(

    private var callback: SongDataSource.Callback<Song>

    ) : AsyncTask<Context, Pair<List<Song>?, Pair<Int, Song>?>, List<Song>>() {

    override fun doInBackground(vararg params: Context?): List<Song> {
        var songs = ContentProvider.getSongs(params[0]!!)
        publishProgress(Pair(songs, null))
        getThumbSongs(params[0]!!, songs)
        return songs
    }

    override fun onPostExecute(result: List<Song>?) {
        this.cancel(true)
    }

    override fun onProgressUpdate(vararg values: Pair<List<Song>?, Pair<Int, Song>?>?) {
        val v = values[0]
        if (v!!.first != null) {
            callback.onGetDataSuccess(v.first!!)
        } else {
            callback.onUpdateData(v!!.second!!)
        }
    }

    private fun getThumbSongs(context: Context, songs: List<Song>) {
        val size = context.resources.getDimensionPixelSize(R.dimen._50sdp)
        for ((index, song) in songs.withIndex()) {
            try {
                val thumb = ContentProvider.getThumbSong(context, song.idAlbum)
                songs[index].thumb = thumb.scaleAndCropCenter(size, size)
                publishProgress(Pair(null, Pair(index, songs[index])))
            } catch (e: Exception) {
            }
        }
    }
}
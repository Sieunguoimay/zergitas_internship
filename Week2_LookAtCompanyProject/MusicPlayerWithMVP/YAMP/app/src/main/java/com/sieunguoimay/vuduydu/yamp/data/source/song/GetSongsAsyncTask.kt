package com.sieunguoimay.vuduydu.yamp.data.source.song

import android.content.Context
import android.os.AsyncTask
import com.sieunguoimay.vuduydu.yamp.R
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import com.sieunguoimay.vuduydu.yamp.data.source.song.provider.ContentProvider
import com.sieunguoimay.vuduydu.yamp.utils.helpers.scaleAndCropCenter

class GetSongsAsyncTask(

    private var callback: SongLocalDataContract.Callback<Song>

    ):AsyncTask<Context,   Pair<List<Song>?,Pair<Int, Song>?>,   List<Song>> (){


    override fun doInBackground(vararg params: Context?): List<Song> {
        var songs = ContentProvider.getSongs(params[0]!!)

        publishProgress(Pair(songs,null))

        getThumbSongs(params[0]!!,songs)

        return songs;
    }



    override fun onProgressUpdate(vararg values: Pair<List<Song>?, Pair<Int, Song>?>?) {
        val v = values[0]
        if(v!!.first!=null){
            callback.onGetDataSuccess(v.first!!)
        }else{
            callback.onUpdateData(v!!.second!!)
        }

    }

    private fun getThumbSongs(context:Context,songs:List<Song>){
        val size = context.resources.getDimensionPixelSize(R.dimen._50sdp)
        for ((index, song) in songs.withIndex()) {
            try {
                val thumb = ContentProvider.getThumbSong(context, song.albumId)
                songs[index].thumb = thumb.scaleAndCropCenter(size, size)
                publishProgress(Pair(null, Pair(index, songs[index])))
            } catch (e: Exception) {
            }
        }
    }

    override fun onPostExecute(result: List<Song>?) {
        this.cancel(true);
    }
}
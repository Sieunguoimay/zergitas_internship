package com.sieunguoimay.vuduydu.s10musicplayer.models.tasks

import android.content.Context
import android.os.AsyncTask
import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.s10musicplayer.models.provider.LocalSongProvider
import com.sieunguoimay.vuduydu.s10musicplayer.utils.scaleAndCropCenter
import com.sieunguoimay.vuduydu.yamp.data.model.Song

class LoadSongDataAsyncTask(
    private var callback:SongDataCallback
):AsyncTask<Pair<Context,ArrayList<Song>>, Pair<ArrayList<Song>?,Pair<Int,Song>?>, Int>() {
    override fun doInBackground(vararg params: Pair<Context, ArrayList<Song>>): Int {
        LocalSongProvider.loadSong(params[0]!!.first,params[0]!!.second)
        publishProgress(Pair(params[0]!!.second, null))
        getThumbSongs(params[0]!!.first,params[0]!!.second)
        return 0
    }

    override fun onPostExecute(result: Int?) {
        this.cancel(true)
    }

    override fun onProgressUpdate(vararg values: Pair<ArrayList<Song>?,Pair<Int, Song>?>?) {
        val v = values[0]
        if(v!!.first!=null){
            //come here on finish loading song
            callback.onSongsLoaded(v!!.first!!)
        }else{
            callback.onThumbailsLoaded()
        }
    }

    private fun getThumbSongs(context: Context, songs: List<Song>) {
        val size = context.resources.getDimensionPixelSize(R.dimen._50sdp)
        for ((index, song) in songs.withIndex()) {
            try {
                val thumb = LocalSongProvider.getThumbail(context, song.albumId)
                songs[index].thumb = thumb.scaleAndCropCenter(size, size)
                publishProgress(Pair(null, Pair(index, songs[index])))
            } catch (e: Exception) {
            }
        }
    }

    interface SongDataCallback{
        fun onThumbailsLoaded()
        fun onSongsLoaded(songList:ArrayList<Song>)
    }
}
package com.sieunguoimay.vuduydu.s10musicplayer.models.tasks

import android.content.Context
import android.os.AsyncTask
import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Category
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Song
import com.sieunguoimay.vuduydu.s10musicplayer.models.provider.LocalSongProvider
import com.sieunguoimay.vuduydu.s10musicplayer.utils.scaleAndCropCenter
//
//Pair<Context,
//Pair<Pair<ArrayList<Song>,LinkedHashMap<Long,Int>>,Pair<>>
//>Pair<Context,Pair<ArrayList<Song>,LinkedHashMap<Long,Int>>>
class LoadSongDataAsyncTask(
    private var callback:SongDataCallback

):AsyncTask<LoadSongDataAsyncTask.Params,
        Pair<ArrayList<Song>?,Pair<Int,Song>?>,
        Int>() {

    class Params(
        var context:Context,
        var songList:ArrayList<Song>,
        var songMap:LinkedHashMap<Long,Int>,
        var albumList:ArrayList<Category>,
        var artistList:ArrayList<Category>
        ){}


    override fun doInBackground(vararg params:Params ): Int {
        LocalSongProvider.loadSong(params[0].context,params[0].songList,params[0].songMap,params[0].albumList,params[0].artistList)
        publishProgress(Pair(params[0].songList, null))
        getThumbailsAndLikeStates(params[0].context,params[0].songList)
        return 0
    }

    override fun onPostExecute(result: Int?) {
        this.cancel(true)
    }

    override fun onProgressUpdate(vararg values: Pair<ArrayList<Song>?,Pair<Int,Song>?>?) {
        val v = values[0]
        if(v!!.first!=null){
            //come here on finish loading song
            callback.onSongsLoaded(v!!.first!!)
        }else{
            callback.onThumbailsLoaded()
        }
    }

    private fun getThumbailsAndLikeStates(context:Context, songs:ArrayList<Song>) {
        val size = context.resources.getDimensionPixelSize(R.dimen._50sdp)
        for ((index, song) in songs.withIndex()) {
            try {
                val thumb = LocalSongProvider.getThumbail(context, song.albumId)
                song.thumb = thumb.scaleAndCropCenter(size, size)
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
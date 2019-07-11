package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity

import android.util.Log
import android.util.LongSparseArray
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Playlist
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Song

private const val TAG = "DATABASE_PRESENTER"
class DatabasePresenter(var model:DatabaseContract.Model):DatabaseContract.Presenter {


    var view: DatabaseContract.View? = null

    override fun likeOrUnlike(song: Song) {
        if(!song.liked) {
            song.liked = true
            model.insertFavouriteSong(song.id,song.title)
            view?.updateOnLike(song)
        }
        else {
            song.liked = false
            model.deleteFavouriteSong(song.id)
            view?.updateOnUnlike(song)
        }
    }

    //this function gets called only onCreate
    override fun getFavouriteSongs(favouriteList:ArrayList<Song>, songList:ArrayList<Song>
                                   ,songMap:LinkedHashMap<Long,Int>,favouriteMap:LinkedHashMap<Int,Long>)
    {
        //since this method gets called many times during running time
        //it must be cleared before updating with new data
        favouriteList.clear()
        val list = model.getFavouriteSongs()
        for((index,fs) in list.withIndex()){
//            fs.song = songList[songMap[fs.songId]!!]
            favouriteList.add(songList[songMap[fs.songId]!!])
            favouriteMap.put(index,fs.songId)

            songList[songMap[fs.songId]!!].liked = true
        }
        view?.updateOnFavouritSongsLoaded(favouriteList.size)
    }





    override fun createPlaylist(playlistTitle: String, songs: ArrayList<Song>) {
        if(songs.size>0) {
            val id = model.insertPlaylist(playlistTitle, songs)
            if (id > 0) {
                view?.showMessageOnPlaylistCreated(id)
            }
        }
    }

    override fun deletePlaylist(playlist: Playlist) {
        model.deltePlaylist(playlist.id)
    }

    override fun getAllPlaylists(
        playlists: ArrayList<Playlist>,
        playlistSongLists: ArrayList<ArrayList<Song>>,
        playlistSongMaps: ArrayList<LinkedHashMap<Int, Long>>,
        songList: ArrayList<Song>,
        songMap: LinkedHashMap<Long, Int>
    ) {
        playlists.clear()
        playlistSongLists.clear()

        var data = model.getAllPlaylists()
        val tempMap = LinkedHashMap<Long,Int>()
        var playlistSongListIndex = 0
        for(songInfo in data.second){
            if(tempMap[songInfo.playlistId]==null){
                tempMap[songInfo.playlistId] = playlistSongListIndex++
                playlistSongLists.add(ArrayList())
                val playlist = data.first[songInfo.playlistId]!!
                playlist.songNum = 1
                playlists.add(playlist)
            }
            data.first[songInfo.playlistId]!!.songNum++
            playlistSongLists[tempMap[songInfo.playlistId]!!].add(songList[songMap[songInfo.songId]!!])
        }
        view?.updateOnPlaylistsLoaded(playlists.size)
    }

}
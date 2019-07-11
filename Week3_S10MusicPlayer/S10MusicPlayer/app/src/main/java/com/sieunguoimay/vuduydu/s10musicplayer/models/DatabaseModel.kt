package com.sieunguoimay.vuduydu.s10musicplayer.models

import android.content.Context
import android.util.Log
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.FavouriteSongInfo
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Playlist
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.PlaylistSongInfo
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Song
import com.sieunguoimay.vuduydu.s10musicplayer.models.provider.FavouriteSongDatabaseHelper
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.DatabaseContract

private const val TAG = "DATABASE_MODEL"

class DatabaseModel(context: Context): DatabaseContract.Model{

    private val database= FavouriteSongDatabaseHelper(context)

    //DatabaseModel.Model
    override fun insertFavouriteSong(songId: Long, title:String) {
        database.favouriteSongsTable.insertFavouriteSong(FavouriteSongInfo(songId, title),database.writableDatabase)
    }

    override fun deleteFavouriteSong(songId: Long) {
        database.favouriteSongsTable.deleteBySongId(songId,database.writableDatabase)
    }

    override fun getFavouriteSongs(): ArrayList<FavouriteSongInfo> {
        return database.favouriteSongsTable.getAllFavoriteSongs(database.readableDatabase)
    }



    override fun insertPlaylist(playlistTitle: String, songs: ArrayList<Song>):Long {

        val playlistId = database.playlistsTable.insertPlaylist(Playlist(playlistTitle),database.writableDatabase)

        Log.d(TAG,"Inserting playlist "+playlistTitle+" "+database.playlistsTable.getPlaylistCount(database.readableDatabase))

        for(song in songs)
            database.playlistSongsTable.insertPlaylistSong(PlaylistSongInfo(song.id, playlistId, playlistTitle),database.writableDatabase)

        Log.d(TAG,"Songs count "+database.playlistSongsTable.getPlaylistSongCount(database.readableDatabase))

        return playlistId
    }

    override fun deltePlaylist(playlistId: Long) {
        database.playlistSongsTable.deleteSongsByPlaylistId(playlistId,database.writableDatabase)
        database.playlistsTable.deleteByPlaylistId(playlistId,database.writableDatabase)
    }

    override fun getAllPlaylists(): Pair<LinkedHashMap<Long,Playlist>, ArrayList<PlaylistSongInfo>> {
        val playlists = database.playlistsTable.getAllPlaylists(database.readableDatabase)
        val songs = database.playlistSongsTable.getAllPlaylistSongs(database.readableDatabase)
        return Pair(playlists,songs)
    }

}
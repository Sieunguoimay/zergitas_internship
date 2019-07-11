package com.sieunguoimay.vuduydu.s10musicplayer.models.data

import com.sieunguoimay.vuduydu.yamp.data.model.Song

class Playlist(
    var title:String
) {
    var id:Long = -1
    var songNum:Int = 0

    companion object{
        var TABLE_NAME ="playlists"

        val COLUMN_ID = "id"
        val COLUMN_TITLE= "playlist_title"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE + " TEXT)"
        val CREATE_UNIQUE = "CREATE UNIQUE INDEX " + TABLE_NAME+"_song_id_title ON " + TABLE_NAME + "("+ COLUMN_TITLE+")"
    }

}
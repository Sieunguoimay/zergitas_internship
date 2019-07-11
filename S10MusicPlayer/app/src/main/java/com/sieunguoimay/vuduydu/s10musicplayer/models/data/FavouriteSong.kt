package com.sieunguoimay.vuduydu.s10musicplayer.models.data


class FavouriteSong(
    var songId:Long,
    var title:String
){
    var id:Long = 0

    companion object{
        val TABLE_NAME="favourite_songs"
        val COLUMN_ID = "id"
        val COLUMN_TITLE = "title"
        val COLUMN_SONG_ID= "song_id"
        val CREATE_TABLE = "CREATE TATBLE "+ TABLE_NAME+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_TITLE+" TEXT,"+COLUMN_SONG_ID+" TEXT)"
    }

}
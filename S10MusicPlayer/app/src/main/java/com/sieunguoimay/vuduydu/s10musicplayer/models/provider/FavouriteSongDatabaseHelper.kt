package com.sieunguoimay.vuduydu.s10musicplayer.models.provider

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.FavouriteSong

const val TAG = "DATABASE_HELPER"

class FavouriteSongDatabaseHelper(
    context: Context,
    DATABASE_NAME:String = "s10_music_player_db",
    DATABASE_VERSION:Int = 1
) : SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(FavouriteSong.CREATE_TABLE)
        Log.d(TAG,"Se")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS "+FavouriteSong.TABLE_NAME)
        onCreate(db)
    }


    fun insertFavouriteSong(song: FavouriteSong):Long{
        Log.d(TAG, "Inserted favourite song "+song.title)

        val values = ContentValues()
        values.put(FavouriteSong.COLUMN_TITLE, song.title)
        values.put(FavouriteSong.COLUMN_SONG_ID, song.songId)

        val id = writableDatabase.insert(FavouriteSong.TABLE_NAME,null, values)
        writableDatabase.close()
        return id
    }

    fun getFavouriteSong(id:Long):FavouriteSong{
        val cursor = readableDatabase.query(FavouriteSong.TABLE_NAME,Array(3){FavouriteSong.COLUMN_ID;FavouriteSong.COLUMN_TITLE; FavouriteSong.COLUMN_SONG_ID},
            FavouriteSong.COLUMN_ID+"=?",Array(1){id.toString()},null, null,null, null)

        if(cursor!=null){
            cursor.moveToFirst()
        }
        val song = FavouriteSong(
            cursor.getLong(cursor.getColumnIndex(FavouriteSong.COLUMN_SONG_ID)),
            cursor.getString(cursor.getColumnIndex(FavouriteSong.COLUMN_TITLE)))
        song.id = cursor.getLong(cursor.getColumnIndex(FavouriteSong.COLUMN_ID))

        cursor.close()
        return song
    }
    fun getAllFavoriteSongs():List<FavouriteSong>{
        val list = ArrayList<FavouriteSong>()
        val selectQuery = "SELECT * FROM "+FavouriteSong.TABLE_NAME+" ORDER BY "+FavouriteSong.COLUMN_TITLE+" DESC"
        val cursor = writableDatabase.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()){
            do{
                val song = FavouriteSong(
                    cursor.getLong(cursor.getColumnIndex(FavouriteSong.COLUMN_SONG_ID)),
                    cursor.getString(cursor.getColumnIndex(FavouriteSong.COLUMN_TITLE)))
                song.id = cursor.getLong(cursor.getColumnIndex(FavouriteSong.COLUMN_ID))

                list.add(song)
            }while(cursor.moveToNext())
        }
        writableDatabase.close()
        return list
    }
    fun getFavouriteSongCount():Int{
        val countQuery = "SELECT * FROM "+ FavouriteSong.TABLE_NAME
        val cursor = readableDatabase.rawQuery(countQuery,null)
        val count = cursor.count
        cursor.close()
        return count
    }
    fun deleteBySongId(songId:Long){
        Log.d(TAG, "Deleted favourite song "+songId)
        writableDatabase.delete(FavouriteSong.TABLE_NAME,FavouriteSong.COLUMN_SONG_ID+"= ?",Array(1){songId.toString()})
        writableDatabase.close()
    }

    fun deleteById(id:Long){
        writableDatabase.delete(FavouriteSong.TABLE_NAME,FavouriteSong.COLUMN_SONG_ID+"= ?",Array(1){id.toString()})
        writableDatabase.close()
    }
}
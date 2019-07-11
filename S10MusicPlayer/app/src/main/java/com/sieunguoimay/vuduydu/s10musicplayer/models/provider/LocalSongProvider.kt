package com.sieunguoimay.vuduydu.s10musicplayer.models.provider

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.sieunguoimay.vuduydu.yamp.data.model.Song

object LocalSongProvider{
    fun loadSong(context: Context,songList:ArrayList<Song>):Boolean{
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.DISPLAY_NAME,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED,
            MediaStore.Audio.AudioColumns.SIZE
        )
        val cursor = context.contentResolver.query(uri,projection,null,null,null)
        if(cursor!=null){

            while(cursor.moveToNext()){
                songList.add(
                    Song(cursor.getLong(0),
                        cursor.getLong(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        null,
                        cursor.getLong(5),
                        cursor.getLong(6),
                        cursor.getLong(7)
                    )
                )
            }
            return true
        }
        return false
    }
    fun getThumbail(context: Context, albumId: Long) : Bitmap {
        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        val uri = ContentUris.withAppendedId(artworkUri, albumId)
        val input = context.contentResolver.openInputStream(uri)
        val thumb = BitmapFactory.decodeStream(input)
        return thumb
    }
}
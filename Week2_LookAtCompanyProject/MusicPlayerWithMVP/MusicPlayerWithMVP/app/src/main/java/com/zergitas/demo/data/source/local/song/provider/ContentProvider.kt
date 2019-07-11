package com.zergitas.demo.data.source.local.song.provider

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.zergitas.demo.data.model.Song

object ContentProvider {
    fun getSongs(context: Context): List<Song> {

        var songs = ArrayList<Song>()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.DISPLAY_NAME,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED,
            MediaStore.Audio.AudioColumns.SIZE,
            MediaStore.Audio.AudioColumns.DURATION
        )
        var cursor = context.contentResolver.query(uri, projection, null, null, null)

        if (cursor.count <= 0) return songs

        while (cursor.moveToNext()) {

            val id = cursor.getLong(ID)
            val idAlbum = cursor.getLong(IDALBUM)
            val name = cursor.getString(NAME)
            val singer = cursor.getString(SINGER)
            val data = cursor.getString(DATA)
            val date = cursor.getLong(DATE)
            val size = cursor.getLong(SIZE)
            val duration = cursor.getLong(DURATION)

            val song = Song(id, idAlbum, name, singer, data, null, duration, date, size)
            songs.add(song)
//            if (songs.size == 10) return songs
        }
        return songs
    }

    fun getThumbSong(context: Context,idAlbum: Long) :Bitmap{
        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        val uri = ContentUris.withAppendedId(artworkUri, idAlbum)
        val input = context.contentResolver.openInputStream(uri)
        val thumb = BitmapFactory.decodeStream(input)
        return thumb
    }

    val ID = 0
    val IDALBUM = 1
    val NAME = 2
    val SINGER = 3
    val DATA = 4
    val DATE = 5
    val SIZE = 6
    val DURATION = 7
}
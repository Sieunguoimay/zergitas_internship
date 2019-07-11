package com.audio.speed.musicchanger.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore

object BitmapUtils {
    fun getThumbAudioFromPath(context: Context, path: String): Bitmap? {
        val idAlbum = getAlbumIdFromPath(context, path)
        return if (idAlbum == null) {
            null
        } else {
            getThumbSongByAlbumID(context, idAlbum)
        }
    }

    @SuppressLint("Recycle")
    fun getAlbumIdFromPath(context: Context, path: String): Long? {
        val contentResolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.DATA + " = '" + path + "'"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

        val cursor = contentResolver.query(
                uri,
                null,
                selection,
                null,
                sortOrder
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            cursor.getLong(idColumn)
        } else {
            null
        }
    }

    fun getThumbSongByAlbumID(context: Context, idAlbum: Long): Bitmap? {
        return try {
            val artworkUri = Uri.parse("content://media/external/audio/albumart")
            val uri = ContentUris.withAppendedId(artworkUri, idAlbum)
            val input = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            null
        }
    }
}
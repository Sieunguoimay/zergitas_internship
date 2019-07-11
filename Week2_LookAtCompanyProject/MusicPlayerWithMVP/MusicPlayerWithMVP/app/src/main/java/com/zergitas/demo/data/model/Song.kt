package com.zergitas.demo.data.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class Song(
    var id: Long,
    var idAlbum: Long,
    var name: String,
    var singer: String,
    var path: String,
    var thumb: Bitmap?,
    var duration: Long,
    var date: Long,
    var size: Long
) : Parcelable {


    constructor(parcel: Parcel)
            : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong()){}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(idAlbum)
        parcel.writeString(name)
        parcel.writeString(singer)
        parcel.writeString(path)
        parcel.writeParcelable(thumb, flags)
        parcel.writeLong(duration)
        parcel.writeLong(date)
        parcel.writeLong(size)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }

    fun copyObject(): Song {
        return Song(
            this.id,
            this.idAlbum,
            this.name,
            this.singer,
            this.path,
            this.thumb,
            this.duration,
            this.date,
            this.size
        )
    }

}
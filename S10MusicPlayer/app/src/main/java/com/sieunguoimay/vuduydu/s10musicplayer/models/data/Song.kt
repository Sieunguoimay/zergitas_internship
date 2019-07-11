package com.sieunguoimay.vuduydu.yamp.data.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable


open class Song(
    var id: Long,
    var albumId: Long,
    var title: String,
    var artist:String,
    var path:String,
    var thumb: Bitmap? = null,
    var duration: Long,
    var date: Long,
    var size:Long
) : Parcelable {



    constructor(parcel: Parcel)
            :this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong())
    {}



    override fun describeContents(): Int {
        return 0;
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeLong(id)
        dest.writeLong(albumId)
        dest.writeString(title)
        dest.writeString(artist)
        dest.writeString(path)
        dest.writeParcelable(thumb,flags)
        dest.writeLong(duration)
        dest.writeLong(date)
        dest.writeLong(size)
    }



    companion object CREATOR:Parcelable.Creator<Song>{
        override fun createFromParcel(source: Parcel?): Song {
            return Song(source!!)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }


    }

    fun copyObject():Song{
        return Song(this.id,this.albumId, this.title, this.artist, this.path, this.thumb, this.duration, this.date, this.size)
    }
}

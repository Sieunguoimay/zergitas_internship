package com.sieunguoimay.vuduydu.s10musicplayer.models.data

import com.sieunguoimay.vuduydu.yamp.data.model.Song

class Playlist(
    var name:String
) {
    var songList = ArrayList<Song>()
}
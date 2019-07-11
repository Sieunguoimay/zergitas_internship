package com.sieunguoimay.vuduydu.yamp.data.source.song

import com.sieunguoimay.vuduydu.yamp.data.model.Song

class SongLocalDataContract {
    interface Callback<T>{
        fun onGetDataSuccess(data: List<T>)
        fun onUpdateData(data: Pair<Int,T>)
        fun onFailed(message:String)
    }
    interface LocalDataSource{
        fun getSongs(callback: Callback<Song>)
    }

}
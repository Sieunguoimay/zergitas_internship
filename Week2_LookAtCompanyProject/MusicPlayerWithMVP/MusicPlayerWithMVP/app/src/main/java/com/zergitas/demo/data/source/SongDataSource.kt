package com.zergitas.demo.data.source

import com.zergitas.demo.data.model.Song

interface SongDataSource {

    interface Callback<T> {
        fun onGetDataSuccess(data: List<T>)

        fun onUpdateData(data:Pair<Int,T>)

        fun onFail(message: String)
    }

    /**
     * local data for song
     */
    interface LocalDataSource {

        fun getSongs(callback: Callback<Song>)

    }
}
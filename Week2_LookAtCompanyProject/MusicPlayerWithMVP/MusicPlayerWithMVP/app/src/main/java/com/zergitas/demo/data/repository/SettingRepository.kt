package com.zergitas.demo.data.repository

import android.content.Context
import com.zergitas.demo.data.source.SettingDataSource
import com.zergitas.demo.data.source.local.setting.Cache

class SettingRepository(context: Context) : SettingDataSource.SettingLocalDataSource {
    private var cache: Cache

    init {
        cache = Cache(context)
    }

    override fun getLoop(callback: SettingDataSource.Callback<Boolean>) {
        callback.onGetDataSuccess(cache.getBoolean(Cache.LOOP_SONG.first))
    }

    override fun onUpdateLoop(callback: SettingDataSource.Callback<Boolean>) {
        val isLoop=cache.getBoolean(Cache.LOOP_SONG.first)
        cache.putBoolean(Cache.LOOP_SONG.first, !isLoop)
        callback.onGetDataSuccess(!isLoop)
    }

}
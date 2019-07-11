package com.zergitas.demo.data.source

interface SettingDataSource {
    interface Callback<T> {
        fun onGetDataSuccess(data: T)
    }

    interface SettingLocalDataSource{
        fun getLoop(callback:Callback<Boolean>)

        fun onUpdateLoop(callback: Callback<Boolean>)
    }
}
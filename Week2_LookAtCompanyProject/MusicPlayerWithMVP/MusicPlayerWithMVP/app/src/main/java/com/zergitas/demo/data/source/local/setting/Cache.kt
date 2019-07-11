package com.zergitas.demo.data.source.local.setting

import android.content.Context
import android.content.SharedPreferences

class Cache(var context: Context) {
    private val NAME = "appdemo"
    private val MODE = Context.MODE_PRIVATE
    private var instance: SharedPreferences

    init {
        instance = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(
        operation: (SharedPreferences.Editor)
        -> Unit
    ) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        instance.edit() {
            it.putBoolean(key, value)
        }
    }

    fun getBoolean(key: String): Boolean = instance.getBoolean(key, false)
    companion object{
        val LOOP_SONG = Pair("is_loop_song", false)
    }
}
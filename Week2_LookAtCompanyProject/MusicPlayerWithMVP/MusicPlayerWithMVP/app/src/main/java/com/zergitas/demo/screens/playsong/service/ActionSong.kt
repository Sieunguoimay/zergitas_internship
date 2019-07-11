package com.zergitas.demo.screens.playsong.service

import android.support.annotation.StringDef


@StringDef(ActionSong.NEXT, ActionSong.PLAY, ActionSong.PLAY_NEW, ActionSong.PRE, ActionSong.CREAT_NOTI)
annotation class ActionSong {
    companion object {
        const val CREAT_NOTI = "com.zergitas.demo.CREATNOTIFICATION"
        const val PLAY_NEW = "com.zergitas.demo.PLAYNEW"
        const val PLAY = "com.zergitas.demo.PLAY"
        const val NEXT = "com.zergitas.demo.NEXT"
        const val PRE = "com.zergitas.demo.PRE"
    }
}
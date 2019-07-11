package com.sieunguoimay.vuduydu.yamp.screens.playsong.interfaces

import android.graphics.Bitmap

interface LoadingThumbCallback {
    fun onSuccess(bitmap: Bitmap)
    fun onFail()
}
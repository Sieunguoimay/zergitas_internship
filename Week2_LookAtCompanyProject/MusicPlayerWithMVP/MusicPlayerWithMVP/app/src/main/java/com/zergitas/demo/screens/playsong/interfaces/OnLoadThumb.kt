package com.zergitas.demo.screens.playsong.interfaces

import android.graphics.Bitmap

interface OnLoadThumb {
    fun onSuccess(bitmap: Bitmap)
    fun onFail()
}
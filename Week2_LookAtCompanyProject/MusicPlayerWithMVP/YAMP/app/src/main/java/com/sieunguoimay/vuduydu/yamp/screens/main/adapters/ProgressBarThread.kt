package com.sieunguoimay.vuduydu.yamp.screens.main.adapters

import android.os.Handler
import android.os.Looper

class ProgressBarThread: Thread(){

    var handler:Handler
    var callback:ProgressCallback? = null
    var progress:Float = 0.0f
    init{
        handler = Handler(Looper.getMainLooper())
    }
    override fun run(){
        while(progress<100){
            handler.post(Runnable {
                progress = callback?.updateProgress()!!
            })
            try {
                sleep(200)
            }catch(e: InterruptedException){
                e.printStackTrace()
            }
        }
    }
    interface ProgressCallback{
        fun updateProgress():Float
    }
}
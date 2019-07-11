package com.sieunguoimay.vuduydu.s10musicplayer.tasks

import android.os.Handler
import android.os.Looper
import android.util.Log

const val TAG = "PROGRESS_BAR_THREAD"
class ProgressBarThread(
    var callback:ProgressCallback
): Thread(){
    var handler: Handler

    var progress:Float = 0.0f
    var running:Boolean = true
    init{
        handler = Handler(Looper.getMainLooper())
    }
    override fun run(){

        Log.d(TAG,"Thread started")

        while(progress<1.0f&&running){
            handler.post{
                progress = callback?.updateProgress()!!
            }
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
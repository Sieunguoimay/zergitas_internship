package com.sieunguoimay.vuduydu.yamp.widgets.canvas

import android.graphics.Canvas
import android.os.Looper
import android.util.Log

class CanvasThread(
    var canvasSurfaceView: CanvasSurfaceView
): Thread(){
    var running:Boolean = false
    override fun run(){
        Log.d("CanvasThread","start running")
        Looper.prepare()
        while(running){
            var c:Canvas? = null
            try{
                c = canvasSurfaceView.holder.lockCanvas()
                synchronized(canvasSurfaceView.holder){
                    canvasSurfaceView.draw(c)
                }
            }finally {
                if(c!=null){
                    canvasSurfaceView.holder.unlockCanvasAndPost(c)
                }
            }
        }
    }

}
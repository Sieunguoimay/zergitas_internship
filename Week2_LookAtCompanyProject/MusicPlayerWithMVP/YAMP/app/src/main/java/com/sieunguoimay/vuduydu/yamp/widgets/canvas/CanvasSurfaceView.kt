package com.sieunguoimay.vuduydu.yamp.widgets.canvas

import android.content.Context
import android.graphics.*
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import com.sieunguoimay.vuduydu.yamp.R

class CanvasSurfaceView(
    context: Context
): SurfaceView(context){
    private var bitmap:Bitmap
    var thread:CanvasThread
    init{
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_song)
        thread = CanvasThread(this)
        holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder?) {
                thread.running = true
                thread.start()
                Log.d("Canvas","Surface created")
            }
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                var retry = true
                thread.running = false
                while(retry){
                    try{
                        thread.join()
                        retry = true
                    }catch (e:InterruptedException){
                        e.printStackTrace()
                    }
                }
                Log.d("Canvas","Surface Destroyed")
            }


        })
    }
    var firstTime = true
    override fun onDraw(canvas:Canvas){
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap!!, null,Rect(100,100,200,200),null)
        if(firstTime){
            firstTime = false
            Toast.makeText(context,"Surface view Drawing",Toast.LENGTH_SHORT).show()
        }

    }
}
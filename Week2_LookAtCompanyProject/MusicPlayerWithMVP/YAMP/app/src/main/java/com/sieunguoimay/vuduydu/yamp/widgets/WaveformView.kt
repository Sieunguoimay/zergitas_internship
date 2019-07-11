package com.sieunguoimay.vuduydu.yamp.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.sieunguoimay.vuduydu.yamp.R
import com.sieunguoimay.vuduydu.yamp.widgets.particle_system.Particle
import com.sieunguoimay.vuduydu.yamp.widgets.particle_system.ParticleSystem

class WaveformView(
    context:Context,
    attributes: AttributeSet
): View(context,attributes) {
    private var waveformRenderer:WaveformRenderer? = null

    private var mBytes: ByteArray? = null
    private var mPoints: FloatArray? = null
    private var mRect = Rect()
    private var mForePaint = Paint()

    //what to draw
    var bitmap: Bitmap
    private val particleSystems = ArrayList<ParticleSystem>()

    init{
        mBytes = null;
        mForePaint.setStrokeWidth(3f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.rgb(0, 128, 255));
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_song)
    }

    fun setRenderer(renderer:WaveformRenderer){
        waveformRenderer = renderer
    }

    override fun onDraw(canvas:Canvas){

        super.onDraw(canvas)
        if (mBytes == null) {
            return
        }
        if (mPoints == null || mPoints?.size!!.compareTo(mBytes?.size!!* 4)<0) {
            mPoints = FloatArray(mBytes?.size!! * 4)
        }
        mRect.set(0, 0, width, height)
        var magnitude:Float = 0.0f
        for (i in 0 until mBytes?.size!!-1) {

            mPoints!![i * 4] = (mRect.width() * i).toFloat() / (mBytes?.size!!.minus(1)).toFloat()

            mPoints!![i * 4 + 1] = mRect.height().toFloat() / 2 + (mBytes!![i] + 128).toByte() * (mRect.height() / 4) / 128

            mPoints!![i * 4 + 2] = mRect.width().toFloat() * (i + 1) / (mBytes?.size!!.minus(1)).toFloat()

            mPoints!![i * 4 + 3] = mRect.height().toFloat() / 2 + (mBytes!![i + 1] + 128).toByte() * (mRect.height() / 4) / 128

            if(i>0)
                magnitude += Math.abs((mPoints!![i * 4 + 3]-mPoints!![i * 4 + 1])-(mPoints!![(i-1) * 4 + 3]-mPoints!![(i-1) * 4 + 1]))

        }

        magnitude/=(mBytes!!.size-2)

        canvas.drawLines(mPoints, mForePaint)

        val x =width.toFloat()/2
        val y =height.toFloat()/2
        val radius = magnitude+100

        if(radius>100)
        if(particleSystems.size<10) {

            var p = ParticleSystem(x, y,Color.WHITE,-0.25f*Math.max(radius,150.0f)/150.0f,Math.max(radius,150.0f)/150.0f*20.5f)
            particleSystems.add(p)

        }
        val iter = particleSystems.iterator()
        while (iter.hasNext()) {
            val p = iter.next()
            p.update()
            p.draw(canvas)
            if (p.hasDone()) iter.remove()
        }

        canvas.drawBitmap(bitmap!!,null,Rect((x-radius).toInt(),(y-radius).toInt(),(x+radius).toInt(),(y+radius).toInt()),null)

    }
    fun updateWaveform(waveform: ByteArray){
        mBytes = waveform
        invalidate()
    }

    interface WaveformRenderer{
        fun render(canvas:Canvas , waveform:ByteArray)
    }
}

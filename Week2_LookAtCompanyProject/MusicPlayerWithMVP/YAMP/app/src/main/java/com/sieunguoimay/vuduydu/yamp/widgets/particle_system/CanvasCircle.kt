package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.animations

import android.graphics.Canvas
import android.graphics.Paint
import com.sieunguoimay.vuduydu.yamp.widgets.particle_system.Vec2

open class CanvasCircle(var x: Float, var y: Float, var radius: Float) {

    var pos: Vec2

    var paint: Paint
        protected set

    init {
        this.pos = Vec2(x, y)
        this.paint = Paint()
    }

    fun setPos(x: Float, y: Float) {
        this.pos.set(x, y)
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(pos.x.toFloat(), pos.y.toFloat(), radius.toFloat(), paint)
    }

}

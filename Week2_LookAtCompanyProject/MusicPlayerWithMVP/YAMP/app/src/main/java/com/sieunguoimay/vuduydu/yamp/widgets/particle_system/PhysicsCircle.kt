package com.sieunguoimay.vuduydu.yamp.widgets.particle_system

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.animations.CanvasCircle

open class PhysicsCircle(x: Float, y: Float, radius: Float) : CanvasCircle(x, y, radius) {
    protected var doneFlag: Boolean = false
    protected var vel: Vec2
    protected var acc: Vec2
    protected var upperBoundX: Float = 0.0f
    protected var upperBoundY: Float = 0.0f

    init {
        doneFlag = false
        vel = Vec2()
        acc = Vec2()
    }

    internal fun setVel(x: Float, y: Float) {
        this.vel[x] = y
    }

    fun setAcc(x: Float, y: Float) {
        this.acc[x] = y
    }

    open fun update() {
        //acceleration
        vel.x += acc.x
        vel.y += acc.y

        //            //friction
        vel.x *= 0.9f
        vel.y *= 0.9f


        pos.x += vel.x
        pos.y += vel.y

    }

    fun done() {
        doneFlag = true
    }

    fun hasDone(): Boolean {
        return doneFlag
    }

}

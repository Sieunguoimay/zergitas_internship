package com.sieunguoimay.vuduydu.yamp.widgets.particle_system


class Particle(x: Float, y: Float, vel: Vec2, acc: Vec2, radius: Float, var time: Int, color: Int) :
    PhysicsCircle(x, y, radius) {
    init {
        this.vel = vel
        this.acc = acc
        this.paint.color = color
    }

     override fun update() {
        super.update()
        time--
        if (time <= 0)
            done()
    }
}
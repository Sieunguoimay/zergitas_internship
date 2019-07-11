package com.sieunguoimay.vuduydu.yamp.widgets.particle_system

import android.graphics.Canvas

class ParticleSystem(x: Float, y: Float,color:Int,
                     accFactor:Float = 0.5f,
                     velFactor:Float = 1.0f,
                     amountFactor:Float = 20.0f
                     ) {

    var particles: MutableList<Particle> = ArrayList()
    var particleNum:Int = 0
    init {

        val random_n = 5 + ( amountFactor* Math.random()).toInt()
        particleNum = random_n

        for (i in 0 until random_n) {
            val random_x = (x + 20.0 * 2.0 * (Math.random() - 0.5f)).toFloat()
            val random_y = (y + 20.0 * 2.0 * (Math.random() - 0.5f)).toFloat()
            val random_t = (10.0f + 20.0f * Math.random()).toInt()
            val random_r = (2.0f + 8.0f * Math.random()).toFloat()
            val random_angle = (2.0 * Math.PI * Math.random()).toFloat()
            val random_acc = accFactor * Math.random().toFloat()

            val dir = Vec2(
                Math.cos(random_angle.toDouble()).toFloat(),
                Math.sin(random_angle.toDouble()).toFloat()
            )
            //-random_acc * dir.x
            val acc =
                Vec2(0.0f, 2.0f)
            val vel = Vec2(velFactor * dir.x, velFactor*dir.y)
            particles.add(Particle(random_x, random_y, vel, acc, random_r, random_t,color))
        }
    }

    fun update() {

        val iter = particles.iterator()
        while (iter.hasNext()) {
            val p = iter.next()
            p.update()
            if (p.hasDone()) iter.remove()
        }

    }

    fun draw(canvas: Canvas) {
        for (a in particles) {
            a.draw(canvas)
        }
    }

    fun hasDone(): Boolean {
        return particles.size == 0
    }
}

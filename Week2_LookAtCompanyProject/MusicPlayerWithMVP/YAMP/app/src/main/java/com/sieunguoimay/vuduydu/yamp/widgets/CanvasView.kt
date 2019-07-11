package com.sieunguoimay.vuduydu.yamp.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceView
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.animations.CanvasCircle
import com.sieunguoimay.vuduydu.yamp.widgets.particle_system.Vec2


class CanvasView(
    context: Context,
    var width: Float,
    var height: Float
) : SurfaceView(context) {

    private val circle: CanvasCircle
    private var ball: Ball? = null
    var secondBall: PhysicsCircle? = null
        private set

    private val BALL_RADIUS = 70
    // a line
    private val line1: Line
    private val line2: Line


    private val line1_target: Vec2
    private val line2_target: Vec2

    private val particleSystems = ArrayList<ParticleSystem>()



    internal inner class Line {
        var pos1: Vec2
        var pos2: Vec2
        var paint = Paint()

        constructor() {
            pos1 = Vec2()
            pos2 = Vec2()
        }

        constructor(pos1: Vec2, pos2: Vec2) {
            this.pos1 = pos1
            this.pos2 = pos2
        }

        fun draw(canvas: Canvas) {
            canvas.drawLine(pos1.x, pos1.y, pos2.x, pos2.y, paint)
        }
    }


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

    internal inner class Ball(
        x: Float,
        y: Float,
        upperBoundX: Float,
        upperBoundY: Float,
        radius: Float,
        private val parent: CanvasView
    ) : PhysicsCircle(x, y, radius) {
        init {
            this.upperBoundX = upperBoundX
            this.upperBoundY = upperBoundY
        }

        private fun onCollisionWithWall() {
            radius *= 0.9f
            if (radius <= 20)
                done()
        }

        override fun update() {
            super.update()
            //bounds
            if (pos.x + radius > upperBoundX) {
                pos.x = upperBoundX - radius
                vel.x = -vel.x
                onCollisionWithWall()
                parent.addParticleSystem(pos.x + radius, pos.y)

            }
            if (pos.y + radius > upperBoundY) {
                pos.y = upperBoundY - radius
                vel.y = -vel.y
                onCollisionWithWall()
                parent.addParticleSystem(pos.x, pos.y + radius)
            }

            if (pos.x - radius < 0) {
                pos.x = radius
                vel.x = -vel.x
                onCollisionWithWall()
                parent.addParticleSystem(pos.x - radius, pos.y)
            }
            if (pos.y - radius < 0) {
                pos.y = radius
                vel.y = -vel.y
                onCollisionWithWall()
                parent.addParticleSystem(pos.x, pos.y - radius)
            }
        }

        fun collision(otherBall: PhysicsCircle) {
            val diffX = otherBall.x - pos.x
            val diffY = otherBall.y - pos.y
            val distanceSqr = diffX * diffX + diffY * diffY
            val distance = Math.sqrt(distanceSqr.toDouble()).toFloat()
            if (distance < radius + otherBall.radius) {
                val nX = diffX / distance
                val nY = diffY / distance
                val speed = Math.sqrt((vel.x * vel.x + vel.y * vel.y).toDouble()).toFloat()
                val newVelX = -nX * speed
                val newVelY = -nY * speed
                vel.set(newVelX, newVelY)
            }
        }
    }

    internal inner class Particle(x: Float, y: Float, vel: Vec2, acc: Vec2, radius: Float, private var time: Int) :
        PhysicsCircle(x, y, radius) {
        init {
            this.vel = vel
            this.acc = acc
            this.paint.setColor(Color.RED)
        }

        public override fun update() {
            super.update()
            time--
            if (time <= 0)
                done()
        }
    }

    internal inner class ParticleSystem(x: Float, y: Float) {

        var particles: MutableList<Particle> = ArrayList()

        init {

            val random_n = 5 + (20.0f * Math.random()).toInt()


            for (i in 0 until random_n) {
                val random_x = (x + 20.0 * 2.0 * (Math.random() - 0.5f)).toFloat()
                val random_y = (y + 20.0 * 2.0 * (Math.random() - 0.5f)).toFloat()
                val random_t = (10.0f + 20.0f * Math.random()).toInt()
                val random_r = (2.0f + 8.0f * Math.random()).toFloat()
                val random_angle = (2.0 * Math.PI * Math.random()).toFloat()
                val random_acc = 0.5f * Math.random().toFloat()

                val dir = Vec2(
                    Math.cos(random_angle.toDouble()).toFloat(),
                    Math.sin(random_angle.toDouble()).toFloat()
                )
                val acc =
                    Vec2(-random_acc * dir.x, -random_acc * dir.y)
                val vel = Vec2(random_t * dir.x, random_t * dir.y)
                particles.add(Particle(random_x, random_y, vel, acc, random_r, random_t))
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

    init {


        line1 = Line(
            Vec2((width / 2).toFloat(), (height / 2).toFloat()),
            Vec2((width / 2).toFloat(), (height / 2).toFloat())
        )
        line2 = Line(
            Vec2((width / 2).toFloat(), (height / 2).toFloat()),
            Vec2((width / 2).toFloat(), (height / 2).toFloat())
        )
        line1_target = Vec2(0f, 0f)
        line2_target = Vec2(0f, 0f)

        line1.paint.setColor(Color.RED)
        line1.paint.setStrokeWidth(5.0f)

        line2.paint.setColor(Color.RED)
        line2.paint.setStrokeWidth(5.0f)


        circle = CanvasCircle(width / 2, height / 2, 100.0f)
        circle.paint.setColor(Color.RED)


        createNewBall(width, height)
        secondBall = null
    }

    fun addParticleSystem(x: Float, y: Float) {
        if (particleSystems.size < 2)
            particleSystems.add(ParticleSystem(x.toFloat(), y.toFloat()))
    }

    private fun createNewBall(w: Float, h: Float) {
        ball = Ball(w / 2, h / 2, w, h, BALL_RADIUS.toFloat(), this)
        ball!!.paint.setColor(Color.RED)
    }

    fun update(pitch: Float, roll: Float, yaw: Float) {


        val slope = Vec2(
            Math.sin(roll.toDouble()).toFloat(),
            Math.sin(pitch.toDouble()).toFloat()
        )
        val dir = Vec2(
            Math.cos(yaw.toDouble()).toFloat(),
            Math.sin(yaw.toDouble()).toFloat()
        )

        line1_target.x = line1.pos1.x + 100.0f * slope.x
        line1_target.y = line1.pos1.y - 100.0f * slope.y

        line2_target.x = line2.pos1.x - 100.0f * dir.x
        line2_target.y = line2.pos1.y + 100.0f * dir.y


        //smooth it
        line1.pos2.x += (line1_target.x - line1.pos2.x) * 0.1f
        line1.pos2.y += (line1_target.y - line1.pos2.y) * 0.1f

        line2.pos2.x += (line2_target.x - line2.pos2.x) * 0.1f
        line2.pos2.y += (line2_target.y - line2.pos2.y) * 0.1f


        ball!!.setAcc(6.0f * slope.x, -6.0f * slope.y)
        if (secondBall != null) {
            secondBall!!.update()
            ball!!.collision(secondBall!!)

        }
        ball!!.update()

        if (ball!!.hasDone())
            createNewBall(width, height)


        val iter = particleSystems.iterator()
        while (iter.hasNext()) {
            val p = iter.next()
            p.update()
            if (p.hasDone()) iter.remove()
        }
    }

    fun getBall(): PhysicsCircle? {
        return ball
    }

    fun createSecondBall(): Boolean {
        if (secondBall == null) {
            secondBall = PhysicsCircle(width / 2, height / 2, BALL_RADIUS.toFloat())
            secondBall!!.paint.setColor(Color.RED)
            return true
        }
        return false
    }




    fun update(progress:Float){
        if(particleSystems.size<10)
            particleSystems.add(ParticleSystem(width/2,height/2))

        for (p in particleSystems)
            p.update()

    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE) //without theme);
//        circle.draw(canvas)
//        line1.draw(canvas)
//        line2.draw(canvas)
//
//        ball!!.draw(canvas)
//        if (secondBall != null)
//            secondBall!!.draw(canvas)
//
        for (p in particleSystems)
            p.draw(canvas)
    }

}

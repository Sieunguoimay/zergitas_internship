package com.sieunguoimay.vuduydu.yamp.widgets.particle_system

public class Vec2 {
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()

    constructor() {
        this.x = 0f
        this.y = 0f
    }

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    operator fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }
}

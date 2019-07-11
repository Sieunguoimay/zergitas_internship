package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.animations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class CanvasCircle {
    protected int radius;
    protected Point pos;
    protected Paint paint;

    public CanvasCircle(int x, int y, int radius){
        this.pos = new Point(x,y);
        this.radius = radius;
        this.paint = new Paint();
    }

    public void setPos(int x, int y){this.pos.set(x,y);}
    public void setRadius(int radius){this.radius =radius;}
    public Paint getPaint(){return paint;}
    public void draw(Canvas canvas){
        canvas.drawCircle(pos.x, pos.y, radius,paint);
    }

    public int getRadius() {
        return radius;
    }

}

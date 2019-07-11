package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.animations;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.utils.MyUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyCanvas extends SurfaceView {

    public class Vec2{
        public Vec2(){
            this.x = 0;
            this.y = 0;
        }
        public Vec2(float x, float y){
            this.x = x;
            this.y = y;
        }
        public void set(float x,float y){
            this.x = x;
            this.y = y;
        }
        public float x;
        public float y;
    }
    class Line{
        public Vec2 pos1;
        public Vec2 pos2;
        public Paint paint = new Paint();

        public Line(){
            pos1 = new Vec2();
            pos2 = new Vec2();
        }
        public Line(Vec2 pos1, Vec2 pos2){
            this.pos1 = pos1;
            this.pos2 = pos2;
        }
        void draw(Canvas canvas){
            canvas.drawLine(pos1.x, pos1.y, pos2.x, pos2.y,paint);
        }
    }



    public class PhysicsCircle extends CanvasCircle{
        protected boolean doneFlag;
        protected Vec2 vel;
        protected Vec2 acc;
        protected int upperBoundX;
        protected int upperBoundY;
        public PhysicsCircle(int x, int y, int radius) {
            super(x, y, radius);
            doneFlag = false;
            vel = new Vec2();
            acc = new Vec2();
        }
        void setVel(float x, float y){this.vel.set(x,y);}
        public void setAcc(float x,float y){this.acc.set(x,y);}

        void update(){
            //acceleration
            vel.x+=acc.x;
            vel.y+=acc.y;

//            //friction
            vel.x*=0.9f;
            vel.y*=0.9f;


            pos.x += vel.x;
            pos.y += vel.y;

        }

        public void done(){
            doneFlag = true;
        }
        public boolean hasDone(){return doneFlag;}
        public float getX(){return (float)pos.x;}
        public float getY(){return (float)pos.y;}

    }

    class Ball extends PhysicsCircle{
        private MyCanvas parent;
        public Ball(int x, int y, int upperBoundX, int upperBoundY, int radius, MyCanvas parent) {
            super(x, y, radius);
            this.upperBoundX = upperBoundX;
            this.upperBoundY = upperBoundY;
            this.parent = parent;
        }
        private void onCollisionWithWall(){
            radius*=0.9;
            MyUtilities.vibrate(activity,50);
            if(radius <= 20)
                done();
        }
        void update(){
            super.update();
            //bounds
            if(pos.x+radius>upperBoundX){
                pos.x = upperBoundX-radius;
                vel.x = -vel.x;
                onCollisionWithWall();
                parent.addParticleSystem(pos.x+radius, pos.y);

            }
            if(pos.y+radius>upperBoundY){
                pos.y = upperBoundY-radius;
                vel.y = -vel.y;
                onCollisionWithWall();
                parent.addParticleSystem(pos.x, pos.y+radius);
            }

            if(pos.x-radius<0){
                pos.x = radius;
                vel.x = -vel.x;
                onCollisionWithWall();
                parent.addParticleSystem(pos.x-radius, pos.y);
            }
            if(pos.y-radius<0){
                pos.y = radius;
                vel.y = -vel.y;
                onCollisionWithWall();
                parent.addParticleSystem(pos.x, pos.y-radius);
            }
        }
        public void collision(PhysicsCircle otherBall){
            float diffX = (otherBall.getX()-pos.x);
            float diffY = (otherBall.getY()-pos.y);
            float distanceSqr = diffX*diffX+diffY*diffY;
            float distance = (float)Math.sqrt(distanceSqr);
            if(distance<radius+otherBall.getRadius()){
                float nX = diffX/distance;
                float nY = diffY/distance;
                float speed = (float)Math.sqrt(vel.x*vel.x+vel.y*vel.y);
                float newVelX = -nX*speed;
                float newVelY = -nY*speed;
                vel.set(newVelX,newVelY);
            }
        }
    }

    class Particle extends PhysicsCircle{
        private int time;
        public Particle(int x, int y, Vec2 vel, Vec2 acc,int radius, int time) {
            super(x, y, radius);
            this.time = time;
            this.vel = vel;
            this.acc = acc;
            this.paint.setColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
        }
        public void update(){
            super.update();
            time --;
            if(time <= 0)
                done();
        }
    }

    class ParticleSystem{

        List<Particle> particles = new ArrayList<>();

        public ParticleSystem(float x, float y){

            int random_n = 5+(int)(20.0f*Math.random());


            for(int i = 0; i<random_n; i++){
                int random_x = (int)(x+20.0f*2.0f*(Math.random()-0.5f));
                int random_y = (int)(y+20.0f*2.0f*(Math.random()-0.5f));
                int random_t = (int)(10.0f+20.0f*Math.random());
                int random_r = (int)(2.0f+8.0f*Math.random());
                float random_angle = (float)(2.0f*Math.PI*Math.random());
                float random_acc = 0.5f*(float)(Math.random());

                Vec2 dir = new Vec2((float)Math.cos(random_angle),(float)Math.sin(random_angle));
                Vec2 acc = new Vec2(-random_acc*dir.x, -random_acc*dir.y);
                Vec2 vel = new Vec2(random_t*dir.x, random_t*dir.y);
                particles.add(new Particle(random_x,random_y,vel, acc,random_r,random_t));
            }
        }
        public void update(){

            Iterator<Particle> iter = particles.iterator();
            while (iter.hasNext()) {
                Particle p = iter.next();
                p.update();
                if (p.hasDone()) iter.remove();
            }

        }
        public void draw(Canvas canvas){
            for(PhysicsCircle a:particles){
                a.draw(canvas);
            }
        }
        public boolean hasDone(){
            return (particles.size()==0);
        }
    }



    private Activity activity;
    //private Bitmap bmFunnyFace
    private int width;
    private int height;

    private CanvasCircle circle;
    private Ball ball;
    private PhysicsCircle ball2 = null;

    private final int BALL_RADIUS = 70;
    // a line
    private Line line1;
    private Line line2;



    private Vec2 line1_target;
    private Vec2 line2_target;

    private List<ParticleSystem> particleSystems = new ArrayList<>();

    public MyCanvas(final Context context,Activity activity, int w,int h) {
        super(context);

        this.activity = activity;
        this.width = w;
        this.height = h;


        line1 = new Line(new Vec2(w/2,h/2),new Vec2(w/2,h/2));
        line2 = new Line(new Vec2(w/2,h/2),new Vec2(w/2,h/2));
        line1_target = new Vec2(0,0);
        line2_target = new Vec2(0,0);

        line1.paint.setColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
        line1.paint.setStrokeWidth(5);

        line2.paint.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark1, null));
        line2.paint.setStrokeWidth(5);


        circle = new CanvasCircle(w/2,h/2,100);
        circle.getPaint().setColor(ResourcesCompat.getColor(getResources(), R.color.white_blue, null));


        createNewBall(w,h);
        ball2 = null;
    }
    public void addParticleSystem(int x, int y){
        if(particleSystems.size()<2)
            particleSystems.add(new ParticleSystem(x,y));
    }
    private void createNewBall(int w,int h){
        ball = new Ball(w/2,h/2,w,h,BALL_RADIUS,this);
        ball.getPaint().setColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
    }

    public void update(float pitch, float roll, float yaw){


        Vec2 slope = new Vec2((float)Math.sin(roll),(float)Math.sin(pitch));
        Vec2 dir = new Vec2((float)Math.cos(yaw),(float)Math.sin(yaw));

        line1_target.x = line1.pos1.x+100.0f*slope.x;
        line1_target.y = line1.pos1.y-100.0f*slope.y;

        line2_target.x = line2.pos1.x-100.0f*dir.x;
        line2_target.y = line2.pos1.y+100.0f*dir.y;


        //smooth it
        line1.pos2.x+=(float)(line1_target.x-line1.pos2.x)*0.1f;
        line1.pos2.y+=(float)(line1_target.y-line1.pos2.y)*0.1f;

        line2.pos2.x+=(float)(line2_target.x-line2.pos2.x)*0.1f;
        line2.pos2.y+=(float)(line2_target.y-line2.pos2.y)*0.1f;


        ball.setAcc(6.0f*slope.x,-6.0f*slope.y);
        if(ball2!=null){
            ball2.update();
            ball.collision(ball2);

        }
        ball.update();

        if(ball.hasDone())
            createNewBall(width, height);


        Iterator<ParticleSystem> iter = particleSystems.iterator();
        while (iter.hasNext()) {
            ParticleSystem p = iter.next();
            p.update();
            if (p.hasDone()) iter.remove();
        }
    }

    public PhysicsCircle getBall(){return ball;}
    public PhysicsCircle getSecondBall(){return ball2;}
    public boolean createSecondBall(){
        if(ball2==null){
            ball2 = new PhysicsCircle(width/2,height/2,BALL_RADIUS);
            ball2.getPaint().setColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
            return true;
        }
        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white_background, null)); //without theme);
        circle.draw(canvas);
        line1.draw(canvas);
        line2.draw(canvas);

        ball.draw(canvas);
        if(ball2!=null)
            ball2.draw(canvas);

        for(ParticleSystem p: particleSystems)
            p.draw(canvas);
    }

}

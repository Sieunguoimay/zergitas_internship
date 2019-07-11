package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.services;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

public class OrientationProvider extends Thread
//        extends AsyncTask<Integer,Float,Float>
{

    private float[] accelerometerValues;
    private float[] magneticFieldValues;

    private SensorManager sensorManager;
    private final SensorEventListener accelerometerListener;
    private final SensorEventListener magneticFieldListener;

    private Context context;

    private final GUI gui;
    private Handler handler;
    private boolean running = false;
    private float[] values = new float[3];
    private float[] R = new float[9];

    public OrientationProvider(Context context, GUI _gui){
        this.gui = _gui;
        this.context = context;

        handler = new Handler(Looper.getMainLooper());

        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    accelerometerValues = event.values;
                }
            }
            @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
        magneticFieldListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                    magneticFieldValues = event.values;
                }
            }
            @Override public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        Sensor aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor mfSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(accelerometerListener,aSensor,SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(magneticFieldListener,mfSensor,SensorManager.SENSOR_DELAY_UI);

        //this is a thread??
//        Timer updateTimer = new Timer("gForceUpdate");
//        updateTimer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                doTheCalculation();
//            }
//        },0,50);
//
    }


    @Override
    public void run() {
        while(running)
            doTheCalculation();
    }

    public void setRunning(boolean running){this.running= running;}
    public boolean getRunning(){return running;}
    private void doTheCalculation(){

        if(accelerometerValues!=null&&magneticFieldValues!=null) {
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
            SensorManager.getOrientation(R, values);
            //radians
//                    values[0] = (float)Math.toDegrees(values[0]);
//                    values[1] = (float)Math.toDegrees(values[1]);
//                    values[2] = (float)Math.toDegrees(values[2]);
            //we are in a new thread. and we want to change the ui which is in the main thread.
            //thus, we need to post a new runnable
            //by using either: - a Handler from main thread; - a Handler from this thread with mainlooper
            //as parameter; - a view.post();--> here we choose the second one because we don't want to pass
            //in any more reference to this class.
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                }
//            });
            gui.updateGUI(values[1], values[2], values[0]);
        }
    }


    public void registerSensorListener(){
        Sensor aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor mfSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(accelerometerListener,aSensor,SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(magneticFieldListener,mfSensor,SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterSensorListener(){
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(magneticFieldListener);
    }
//
//    @Override
//    protected Float doInBackground(Integer... integers) {
//        if(accelerometerValues!=null&&magneticFieldValues!=null){
//            float [] values = new float[3];
//            float [] R = new float[9];
//            SensorManager.getRotationMatrix(R,null,accelerometerValues,magneticFieldValues);
//            SensorManager.getOrientation(R,values);
//            //radians
//            gui.updateGUI(values[1],values[2],values[0]);
//        }
//        return null;
//    }


    public interface GUI{

        void updateGUI(float pitch, float roll, float yaw);
    }
}

package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.animations.MyCanvas;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens.TCPMessengerActivity;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens.WifiP2PActivity;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens.WifiScannerActivity;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.services.CanvasNetworkProvider;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.services.OrientationProvider;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CanvasFragment extends Fragment implements OrientationProvider.GUI,CanvasNetworkProvider.MessageHandler {


    private static final int REQUEST_P2P_CONNECTION_CODE = 10002;
    private RelativeLayout view;
    private OrientationProvider orientationProvider;
    private MyCanvas myCanvas;
    private TextView tvBottomDisplay;

    private CanvasNetworkProvider networkProvider;
    public CanvasFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_canvas, container, false);
        view = ((RelativeLayout)v.findViewById(R.id.rlCanvasScreen));
        tvBottomDisplay = v.findViewById(R.id.tvBottomDisplay);

        networkProvider = new CanvasNetworkProvider(this);
        setHasOptionsMenu(true);
        initCanvas();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_canvas,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.it_connection) {
            Intent intent = new Intent(getActivity(), WifiScannerActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.it_sendMessage) {
            Intent intent = new Intent(getActivity(), TCPMessengerActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.it_findFriend) {
            Intent intent = new Intent(getActivity(), WifiP2PActivity.class);
            startActivityForResult(intent,REQUEST_P2P_CONNECTION_CODE,null);
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_P2P_CONNECTION_CODE&&resultCode == RESULT_OK){
            //here we start the network service for communication.

            Bundle bundle = data.getBundleExtra("data");
            if(bundle.getChar("type")=='s'){
//                if(myCanvas.createSecondBall())
//                    Toast.makeText(getContext(), "Connection established (Server) - ball",Toast.LENGTH_SHORT).show();
//                else
                    Toast.makeText(getContext(), "Connection established (Server)",Toast.LENGTH_SHORT).show();

                networkProvider.createServer();

            }else if(bundle.getChar("type")=='c'){
//                if(myCanvas.createSecondBall())
//                    Toast.makeText(getContext(), "Connection established (Client) - ball",Toast.LENGTH_SHORT).show();
//                else
                    Toast.makeText(getContext(), "Connection established (Client)",Toast.LENGTH_SHORT).show();

                networkProvider.createClient(bundle.getString("host_addr"));
            }
        }
    }

    private void initCanvas(){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myCanvas = new MyCanvas(getContext(),getActivity(),metrics.widthPixels,metrics.heightPixels*3/4);
        orientationProvider = new OrientationProvider(getContext(),this);
        myCanvas.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                orientationProvider.setRunning(true);
                orientationProvider.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopOrientationProviderThread();
            }
        });
        view.addView(myCanvas);
    }

    private void stopOrientationProviderThread(){
        orientationProvider.setRunning(false);
        try {
            orientationProvider.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean displaying = false;
    public void setDisplaying(boolean displaying){this.displaying = displaying;}

    @Override
    public void onResume(){
        super.onResume();
        orientationProvider.registerSensorListener();
        initCanvas();
    }
    @Override
    public void onPause(){
        orientationProvider.unregisterSensorListener();
        stopOrientationProviderThread();
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        networkProvider.stopConnection();
    }
    @Override
    public void updateGUI(float pitch, float roll, float yaw) {

        //here we come the message from canvas thread.
        if(displaying){
            myCanvas.update(pitch, roll, yaw);
            Canvas c = myCanvas.getHolder().lockCanvas();
            myCanvas.draw(c);
            myCanvas.getHolder().unlockCanvasAndPost(c);

            //here we send away some data such as position of the ball
            int x = (int)myCanvas.getBall().getX();
            int y = (int)myCanvas.getBall().getY();
            int radius = (int)myCanvas.getBall().getRadius();

            if(orientationProvider.getRunning())
                if(networkProvider.isConnected())
                    networkProvider.sendMessageFromThread("pos "+x+" "+y+" "+radius+" ");
        }
    }

    @Override
    public void onMessageArrive(final String data) {
        //this message comes from the network thread.
        //here we receive data and update the second ball

        tvBottomDisplay.post(new Runnable() {
            public void run() {

                tvBottomDisplay.setText(data);

                String[] numbers = data.split(" ");

                int x = Integer.parseInt(numbers[1]);
                int y = Integer.parseInt(numbers[2]);
                int radius = Integer.parseInt(numbers[3]);

                if(myCanvas.getSecondBall()!=null) {
                    myCanvas.getSecondBall().setPos(x, y);
                    myCanvas.getSecondBall().setPos(x, y);
                    myCanvas.getSecondBall().setRadius(radius);
                    tvBottomDisplay.setText(data+" OK");
                }else
                    myCanvas.createSecondBall();
            }
        });

    }
}

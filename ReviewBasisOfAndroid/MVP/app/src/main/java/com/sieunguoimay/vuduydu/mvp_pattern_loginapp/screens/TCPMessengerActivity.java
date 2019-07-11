package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPMessengerActivity extends AppCompatActivity {

    private EditText etIpAddress;
    private EditText etMessage;
    private CardView btSendMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpmessenger);
        etIpAddress = findViewById(R.id.etIPAddress);
        etMessage = findViewById(R.id.etMessage);
        btSendMessage= findViewById(R.id.btSendMessage);
        btSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick(v);
            }
        });

        Thread myThread = new Thread(new MyServer());
        myThread.start();
    }


    public void buttonClick(View v){
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(etIpAddress.getText().toString(),etMessage.getText().toString());
        Toast.makeText(this, "Button clicked" , Toast.LENGTH_SHORT).show();
    }

    class MyServer implements Runnable {
        ServerSocket serverSocket;
        Socket mySocket;
        DataInputStream dis;
        String message;
        Handler handler = new Handler(getMainLooper());
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Waiting for client " , Toast.LENGTH_SHORT).show();
                    }
                });
                while(true){
                    mySocket = serverSocket.accept();
                    dis = new DataInputStream(mySocket.getInputStream());
                    message = dis.readUTF();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Message received: "+message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class BackgroundTask extends AsyncTask<String ,Void,String> {

        Socket socket;
        DataOutputStream dos;
        String ip, message;

        @Override
        protected String doInBackground(String... strings) {
            ip = strings[0];
            message = strings[1];
            try {
                socket = new Socket(ip, 9700);
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(message);
                dos.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

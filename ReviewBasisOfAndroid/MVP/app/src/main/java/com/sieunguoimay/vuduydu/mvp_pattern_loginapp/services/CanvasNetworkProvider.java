package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.services;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens.WifiP2PActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CanvasNetworkProvider {

    private static final int MESSAGE_READ = 1;
    private SendReceive sendReceive;

    private Server server;
    private Client client;

    private MessageHandler messageHandler;
    private boolean connected;

    //public usage--------------------------------------------------------------
    public CanvasNetworkProvider(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
        connected = false;
    }

    public void createServer(){
       server = new Server();
       server.start();
       connected = true;
    }
    public void createClient(String hostAdd){
        client = new Client(hostAdd);
        client.start();
        connected = true;
    }

    public void stopConnection(){
        if(server!=null){
            try {
                server.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(client!=null){
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        connected = false;
    }

    public void sendMessageFromUIThread(final String msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendReceive.write(msg.getBytes());
            }
        }).start();
    }
    public void sendMessageFromThread(String msg){
        if(sendReceive!=null)
            sendReceive.write(msg.getBytes());
    }
    public boolean isConnected(){
        return (connected);
    }
    //up to this point ---------------------------------------------------------





    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case MESSAGE_READ:
                    byte[] readBuff = (byte[])msg.obj;
                    String tempMsg = new String(readBuff,0, msg.arg1);
                    messageHandler.onMessageArrive(tempMsg);
                    break;
            }
            return false;
        }
    });

    public class Server extends  Thread{
        Socket socket;
        ServerSocket serverSocket;
        @Override
        public void run(){
            try{
                serverSocket = new ServerSocket(9700);
                socket = serverSocket.accept();
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    private class SendReceive extends Thread{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;
        public SendReceive(Socket socket){
            this.socket = socket;
            try{
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;
            while(socket!=null){
                try {
                    bytes = inputStream.read(buffer);
                    if(bytes>0){
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class Client extends Thread{
        Socket socket;
        String hostAddr;
        public Client(String hostAddr){
            this.hostAddr = hostAddr;
            socket = new Socket();
        }
        @Override
        public void run(){
            try{
                socket.connect(new InetSocketAddress(hostAddr,9700),500);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public interface MessageHandler{
        void onMessageArrive(String data);
    }
}

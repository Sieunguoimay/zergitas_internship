package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.services.WifiDirectBroadcastReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WifiP2PActivity extends AppCompatActivity {

    private CardView btDiscover;
    private TextView tvDiscover;
    private TextView tvConnectionStatus;
    public void setConnectionStatusTextView(String text){tvConnectionStatus.setText(text);}
//    private CardView btSendMsg;
//    private TextView tvP2pMessage;

    private WifiManager wifiManager;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    private ListView listView;
    private List<WifiP2pDevice> resultList = new ArrayList<>();
    private List<String>arrayList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    private static final int MESSAGE_READ = 1;

    private Server server;
    private Client client;
    private SendReceive sendReceive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_p2_p);
        initialWork();
        initListener();
    }

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case MESSAGE_READ:
                    byte[] readBuff = (byte[])msg.obj;
                    String tempMsg = new String(readBuff,0, msg.arg1);
                    Toast.makeText(getApplicationContext(),"New message: "+tempMsg, Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });

    void initListener(){

        btDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        tvConnectionStatus.setText("Discovering..");
                    }

                    @Override
                    public void onFailure(int reason) {
                        tvConnectionStatus.setText("Discovery starting failed");
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device = resultList.get(position);
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Connected to "+device.deviceName,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(getApplicationContext(), "Not connected",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
//
//        btSendMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String msg = tvP2pMessage.getText().toString();
//                        sendReceive.write(msg.getBytes());
//                        //Toast.makeText(getApplicationContext(), "Sent a message "+ msg,Toast.LENGTH_SHORT).show();
//                        tvP2pMessage.setText("");
//                    }
//                }).start();
//            }
//        });
    }
    private void initialWork(){
        btDiscover = findViewById(R.id.btDiscover);
        tvDiscover = findViewById(R.id.tvDiscover);
        tvConnectionStatus = findViewById(R.id.tvConnectionStatus);
//        btSendMsg = findViewById(R.id.btSendMsg);
//        tvP2pMessage = findViewById(R.id.tvP2pMessage);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        manager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this,getMainLooper(),null);
        receiver = new WifiDirectBroadcastReceiver(manager,channel, this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        listView = findViewById(R.id.lvDeviceList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark1));
        }

    }

    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            if(!peerList.getDeviceList().equals(resultList)){
                resultList.clear();
                resultList.addAll(peerList.getDeviceList());

                arrayList.clear();
                for(WifiP2pDevice device:resultList){
                    arrayList.add(device.deviceName);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            if(arrayList.size()==0){
                Toast.makeText(getApplicationContext(),"No device found", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
            if(info.groupFormed&&info.isGroupOwner){
                tvConnectionStatus.setText("Host");
//                server = new Server();
//                server.start();
                //have you done yet??????
                //if yes, Peer2Peer is over here. we now obtain the IP address of the server and that's it.

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putChar("type",'s');
                intent.putExtra("data",bundle);
                setResult(RESULT_OK,intent);
                finish();
            }else{
//                tvConnectionStatus.setText("Client (Host: "+groupOwnerAddress+")");
//                client = new Client(groupOwnerAddress);
//                client.start();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putChar("type",'c');
                bundle.putString("host_addr",groupOwnerAddress.getHostAddress());
                intent.putExtra("data",bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,intentFilter);
        }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onStop(){
        super.onStop();
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
    }


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
        public Client(InetAddress hostAddress){
            hostAddr = hostAddress.getHostAddress();
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
}

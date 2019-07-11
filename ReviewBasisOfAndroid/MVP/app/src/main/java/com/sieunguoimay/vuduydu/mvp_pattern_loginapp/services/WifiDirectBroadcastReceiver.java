package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens.WifiP2PActivity;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiP2PActivity activity;
    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,WifiP2PActivity activity){
        this.manager = manager;
        this.channel = channel;
        this.activity =activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context,"Wifi is On",Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(context,"Wifi is Off",Toast.LENGTH_SHORT).show();

        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            if(manager!=null){
                manager.requestPeers(channel,activity.peerListListener);
            }
        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            if(manager!=null){
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if(networkInfo.isConnected()){
                    manager.requestConnectionInfo(channel,activity.connectionInfoListener);
                }else{
                    activity.setConnectionStatusTextView("It's a disconnect");
                }
            }
        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){

        }

    }
}

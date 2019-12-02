package com.lpen.mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

public class NetBroadcast extends BroadcastReceiver {

    private NetworkUtil.WifiStatus lastStatus = NetworkUtil.WifiStatus.NETWORK_NONE;

    public NetBroadcast() {

    }

    public NetBroadcast(Context context) {
        lastStatus = NetworkUtil.getNetWorkState(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        // 网络更改广播. android.net.conn.CONNECTIVITY_CHANGE
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            NetworkUtil.WifiStatus state = NetworkUtil.getNetWorkState(context);
            if (onNetStateListener != null && !TextUtils.isEmpty(state.name()) && !state.name().equals(lastStatus.name())) {
                onNetStateListener.onStateChange(state);
                lastStatus = state;
            }
        }
    }

    private OnNetStateListener onNetStateListener;

    public void setOnNetStateListener(OnNetStateListener onNetStateListener) {
        this.onNetStateListener = onNetStateListener;
    }

    public interface OnNetStateListener {
        void onStateChange(NetworkUtil.WifiStatus state);
    }
}
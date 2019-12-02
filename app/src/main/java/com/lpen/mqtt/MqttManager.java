package com.lpen.mqtt;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.lpen.mqtt.paho.MqttAndroidClient;
import com.lpen.mqtt.paho.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MqttManager {

    private static final int LOOP_INTERVAL = 10;

    public static final int STATUS_SUCCESS = 0x01;  // 订阅成功
    public static final int STATUS_FAILURE = 0x02;  // 订阅/连接失败
    public static final int STATUS_LOST = 0x03;     // 连接中断

    private int mStatus = STATUS_FAILURE;

    @SuppressLint("StaticFieldLeak")
    private static MqttManager mManager;

    private static final int quality = 2;    // 发送质量（0、1、2）

    private Context mContext;

    private MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    private String host;
    private String userName;  // 用户名
    private String passWord;   // 密码

    private String subTopic;
    private String clientId = "";     // 客户端唯一标示

    private Disposable mLoopDisposable;

    private OnMqttMessageListener onMessageListener;

    private long subscribeTime = 0;

    public static MqttManager getInstance(Context context) {
        if (mManager == null) {
            synchronized (MqttManager.class) {
                if (mManager == null)
                    mManager = new MqttManager(context);
            }
        }
        return mManager;
    }

    private MqttManager(Context context) {
        this.mContext = context;
    }

    public void setParams(String... params) {
        host = params[0];
        userName = params[1];
        passWord = params[2];
    }

    private void initOption() {
        conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);   // 清除缓存
        conOpt.setConnectionTimeout(10);    // 设置超时时间，单位：秒
        conOpt.setKeepAliveInterval(300);   // 心跳包发送间隔，单位：秒  听说微信的心跳包是 300s
        // conOpt.setAutomaticReconnect(true);   // 是否自动重连； 目前使用中，打开自动重连会 Crash
        conOpt.setUserName(userName);
        conOpt.setPassword(passWord.toCharArray());
        conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
    }

    public void subTopic(String subTopic) {
        if (TextUtils.isEmpty(subTopic)) {
            if (TextUtils.isEmpty(this.subTopic)) {
                return;
            }
        } else {
            this.subTopic = subTopic;
        }
        initOption();
        clientId = System.currentTimeMillis() + "";  // 以时间戳为clientID，可以避免同一个clientId同事订阅导致只有一个可以接收到消息
        if (client != null) {
            disConnectMqtt(false);
        }
        client = new MqttAndroidClient(mContext, host, clientId);
        client.setCallback(mqttCallback);
        try {
            // 注意： 当订阅的 topic 中包含 通配符时，不能同时设置 遗嘱消息
//            conOpt.setWill(subTopic, "will message".getBytes(Charset.forName("utf-8")), quality, false);
            client.connect(conOpt, null, iMqttActionListener);
        } catch (MqttException e) {
            mStatus = STATUS_FAILURE;
            mqttStatusChangeListener.onChange(mStatus);
            e.printStackTrace();
        }

        loopConnectStatus();
    }

    private void disConnectMqtt(boolean fromUser) {
        if (client == null) return;
        try {
            if (client.isConnected()) {
                client.disconnect();
                client = null;
            }
            if (fromUser) { // 实现用户主动断开时，清空 subTopic, 并且置空 client，避免走到重连方法再次订阅
                subTopic = "";
                client = null;
            }
            mStatus = STATUS_FAILURE;
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void restartServiceIfDestroy() {
        if (client == null) {
            mStatus = STATUS_FAILURE;
            mqttStatusChangeListener.onChange(mStatus);
            return;
        }
        boolean isConnect = client.isConnected();
        if (!isConnect || !checkServiceLife()) {  // MQTTService 被回收
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
                mStatus = STATUS_FAILURE;
                mqttStatusChangeListener.onChange(mStatus);
            }
        }
    }

    private boolean checkServiceLife() {
        ActivityManager myManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (myManager == null) {
            return false;
        }
        ArrayList<ActivityManager.RunningServiceInfo> runningService =
                (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(MqttService.class.getName())) {
                return true;
            }
        }
        return false;
    }

    private void loopConnectStatus() {
        cancelLoop();
        if (TextUtils.isEmpty(subTopic)) return;
        mLoopDisposable = Observable.interval(LOOP_INTERVAL, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        switch (mStatus) {
                            case STATUS_SUCCESS: {
                                restartServiceIfDestroy();
                            }
                            break;
                            case STATUS_FAILURE:
                            case STATUS_LOST: {
                                subTopic(subTopic);
                            }
                            break;
                            default:
                                break;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loopConnectStatus();
                    }
                });
    }

    private void cancelLoop() {
        if (mLoopDisposable != null) {
            mLoopDisposable.dispose();
            mLoopDisposable = null;
        }
    }

    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            if (mStatus != STATUS_SUCCESS) {
                try {
                    if (client != null) {
                        client.subscribe(subTopic, quality);
                        subscribeTime = System.currentTimeMillis();
                        mStatus = STATUS_SUCCESS;
                        mqttStatusChangeListener.onChange(mStatus);
                        publishEmptyMsg();  // 连接之后发一条空的消息，可删除消息队列里面的retain消息
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                    mStatus = STATUS_FAILURE;
                    mqttStatusChangeListener.onChange(mStatus);
                }
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            mStatus = STATUS_FAILURE;
            mqttStatusChangeListener.onChange(mStatus);
        }
    };

    private MqttCallback mqttCallback = new MqttCallback() {

        MqttMessage msg;

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            if (message != null && message != msg && message.getPayload() != null && message.getPayload().length >= 3) {
                // 刚订阅上后的 500 毫秒消息过滤，一般都是遗留的消息重新发过来
                if (System.currentTimeMillis() - subscribeTime < 500) {
                    return;
                }
                msg = message;  // 过滤重复消息
                if (onMessageListener != null) {
                    onMessageListener.onArrived(topic, msg.toString());
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            try {
                if (arg0 != null && arg0.getMessage() != null && arg0.getMessage().getPayload() != null) {
                    String message = arg0.getMessage().toString();
                    if (onMessageListener != null) {
                        onMessageListener.onDelivery(message);
                    }
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void connectionLost(Throwable arg0) {    // 连接中断
            mStatus = STATUS_LOST;
            mqttStatusChangeListener.onChange(mStatus);
        }
    };

    private MqttStatusChangeListener mqttStatusChangeListener = new MqttStatusChangeListener() {
        @Override
        public void onChange(int status) {
            loopConnectStatus();
        }
    };

    private void publishEmptyMsg() {
        publish(clientId, new byte[]{});
    }

    public boolean publish(String topic, String message) {
        return publish(topic, message.getBytes(Charset.forName("utf-8")));
    }

    public boolean publish(String topic, byte[] msg) {
        if (TextUtils.isEmpty(topic)) {
            return false;
        }

        // qos 表示消息的发送模式： 0. 最多分发一次， 1. 至少分发一次， 2. 刚好分发一次
        int qos = 2;
        try {
            client.publish(topic, msg, qos, false);
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getStatus() {
        return mStatus;
    }

    public void addOnMeesageListener(OnMqttMessageListener listener) {
        this.onMessageListener = listener;
    }

    public static void release() {
        if (mManager != null) {
            mManager.releaseMqtt();
        }
        mManager = null;
    }

    private void releaseMqtt() {
        subTopic = "";
        clientId = "";
        cancelLoop();
        disConnectMqtt(true);
    }

}
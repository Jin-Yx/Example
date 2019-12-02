package com.lpen.ui.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lpen.R
import com.lpen.mqtt.MqttManager
import com.lpen.mqtt.NetBroadcast
import com.lpen.mqtt.NetworkUtil
import com.lpen.mqtt.OnMqttMessageListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mqtt.*
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * TODO， 还需要加一个广播，监听网络连接状态改变，切换网络一般 MQTT 都需要重连
 *
 * @author android_P
 * @date 19-12-2
 */
class MqttActivity : AppCompatActivity(), OnMqttMessageListener {

    private var mDisposable: Disposable? = null
    private var mIntervalDisposable: Disposable? = null

    private var mNetBroad: NetBroadcast? = null

    private val mqttManager: MqttManager by lazy {
        MqttManager.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mqtt)

        rgpProtocol.setOnCheckedChangeListener { _, checkedId ->
            rbtTCP.isChecked = checkedId == R.id.rbtTCP
            rbtWS.isChecked = checkedId == R.id.rbtWS
        }

        txtSub.setOnClickListener {
            subTopic()
        }

        txtPub.setOnClickListener {
            pubTopic()
        }

        mqttManager.addOnMeesageListener(this)

        registerBroad()
    }

    private fun registerBroad() {
        if (mNetBroad == null) {
            mNetBroad = NetBroadcast(this)
        }
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mNetBroad!!.setOnNetStateListener {
            if (it == NetworkUtil.WifiStatus.NETWORK_MOBILE || it == NetworkUtil.WifiStatus.NETWORK_WIFI) {
                mqttManager.subTopic(null)
            }
        }
        registerReceiver(mNetBroad, filter)
    }

    private fun checkParams(): Boolean {
        return when {
            editIP.text.isNullOrEmpty() -> {
                Toast.makeText(this, "请输入 IP 地址", Toast.LENGTH_SHORT).show()
                false
            }
            editPort.text.isNullOrEmpty() -> {
                Toast.makeText(this, "请输入端口号", Toast.LENGTH_SHORT).show()
                false
            }
            editAdmin.text.isNullOrEmpty() -> {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun subTopic() {
        if (checkParams()) {
            val builder = StringBuilder()
            if (rbtTCP.isChecked) {
                builder.append("tcp://")
            } else {
                builder.append("ws://")
            }
            builder.append("${editIP.text}:")
            builder.append(editPort.text)

            val admin = editAdmin.text.toString()
            val password  = editPwd.text.toString()
            mqttManager.setParams(builder.toString(), admin, password)
            val subTopic = editSubTopic.text?.toString()
            if (subTopic.isNullOrEmpty()) {
                Toast.makeText(this, "请输入订阅的 Topic", Toast.LENGTH_SHORT).show()
            } else {
                mqttManager.subTopic(subTopic)
            }
        }
    }

    private fun pubTopic() {
        if (txtPub.text == "停止") {
            mDisposable?.dispose()
            txtPub.text = "发布"
            return
        }
        if (mqttManager.status != MqttManager.STATUS_SUCCESS) {
            Toast.makeText(this, "请先完成订阅再发消息", Toast.LENGTH_SHORT).show()
            return
        }
        if (checkParams()) {
            val pubTopic = editPubTopic.text?.toString()
            val pubMessage = editPubContent.text?.toString()
            when {
                pubTopic.isNullOrEmpty() -> {
                    Toast.makeText(this, "请输入发布的 Topic", Toast.LENGTH_SHORT).show()
                }
                pubMessage.isNullOrEmpty() -> {
                    Toast.makeText(this, "请输入要发布的内容", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val count = max(1, editPubCount.text?.toString()?.toIntOrNull() ?: 0)
                    val interval = max(1, editPubInterval.text?.toString()?.toLongOrNull() ?: 10)
                    txtPub.text = "停止"
                    loopPublishMessage(pubTopic, pubMessage, interval, count)
                }
            }
        }
    }

    private fun loopPublishMessage(pubTopic: String, pubMessage: String, time: Long, sum: Int) {
        mDisposable?.dispose()
        mDisposable = Observable.create<Boolean> { emitter ->
            var count = 0
            mIntervalDisposable?.dispose()
            mIntervalDisposable = Observable.interval(time, TimeUnit.SECONDS)
                .subscribe {
                    count ++
                    val over = mqttManager.publish(pubTopic, pubMessage)
                    Log.i("MqttActivity", "第 $count 次发布${if (over) "成功" else "失败"}")
                    if (count >= sum) {
                        mIntervalDisposable?.dispose()
                        emitter.onNext(true)
                    }
                }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mIntervalDisposable?.dispose()
                mDisposable?.dispose()
                txtPub.text = "发布"
            }, {
                mIntervalDisposable?.dispose()
                mDisposable?.dispose()
                txtPub.text = "发布"
            })
    }

    override fun onArrived(topic: String, message: String) {
        Log.i("MqttActivity", "接收到来自 $topic 的消息： $message")
    }

    override fun onDelivery(message: String) {
        Log.i("MqttActivity", "发送的消息： $message")
    }

    override fun onDestroy() {
        try {
            mNetBroad?.apply {
                unregisterReceiver(mNetBroad)
                mNetBroad = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mDisposable?.dispose()
        mIntervalDisposable?.dispose()
        MqttManager.release()
        super.onDestroy()
    }

}
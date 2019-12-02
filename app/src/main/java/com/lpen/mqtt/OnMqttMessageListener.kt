package com.lpen.mqtt

/**
 * @author android_P
 * @date 19-12-2
 */
interface OnMqttMessageListener {

    /**
     * 接收到 某个 @param topic 的 @param message
     */
    fun onArrived(topic: String, message: String)

    /**
     * 发送的消息
     */
    fun onDelivery(message: String)

}
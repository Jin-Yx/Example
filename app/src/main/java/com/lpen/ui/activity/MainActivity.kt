package com.lpen.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lpen.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onGaussianBlurClick(view: View) {
        startActivity(Intent(this, BlurActivity::class.java))
    }

    fun onIJKPlayerClick(view: View) {
        startActivity(Intent(this, VideoActivity::class.java))
    }

    fun onMqttClick(view: View) {
        startActivity(Intent(this, MqttActivity::class.java))
    }
}

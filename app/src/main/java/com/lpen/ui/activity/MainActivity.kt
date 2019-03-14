package com.lpen.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lpen.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBtn.text = "高斯模糊"
    }

    fun onBtnClick(view: View) {
        startActivity(Intent(this, BlurActivity::class.java))
    }
}

package com.lpen.jetpack.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lpen.jetpack.R
import com.lpen.jetpack.databinding.ActivityUiBinding

/**
 * @author LPen
 * @date 19-3-10
 */
class UIActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityUiBinding>(this, R.layout.activity_ui)
    }

}
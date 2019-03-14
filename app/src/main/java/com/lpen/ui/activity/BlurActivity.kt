package com.lpen.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lpen.R
import com.lpen.helper.blur.GlideBlurTransformation
import kotlinx.android.synthetic.main.activity_blur.*

class BlurActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blur)

        Glide
            .with(this)
            .load("https://i0.hdslb.com/bfs/archive/13e279a33a1726cf63d6c1bb267a36a1b005c4e6.jpg")
            .into(ivOriginal)

        Glide
            .with(this)
            .load("https://i0.hdslb.com/bfs/archive/13e279a33a1726cf63d6c1bb267a36a1b005c4e6.jpg")
            .apply(RequestOptions.bitmapTransform(GlideBlurTransformation(this)))
            .into(ivBlur)
    }

}
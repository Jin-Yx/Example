package com.lpen.jetpack.widget

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.lpen.jetpack.R


/**
 * @author pen
 * @date 19-3-16
 */
class TitleBar(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs) {

    constructor(context: Context) : this(context, null)

    private var textView: TextView? = null

    init {
        navigationIcon = ContextCompat.getDrawable(context, R.drawable.icon_back)
        setNavigationOnClickListener {
            if (context is Activity) {
                context.onBackPressed()
            }
        }

        textView = TextView(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        textView!!.gravity = Gravity.CENTER
        textView!!.maxLines = 1
        // textView!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
        textView!!.ellipsize = TextUtils.TruncateAt.END

        addView(textView, lp)

        if (attrs != null) {
            val typedArray: TypedArray? = context.obtainStyledAttributes(attrs,
                R.styleable.TitleBar, 0, 0)
            val defaultColor = ContextCompat.getColor(context, R.color.color_white)
            val titleColor = typedArray?.getColor(R.styleable.TitleBar_titleColor, defaultColor) ?: defaultColor
            val titleSize = typedArray?.getDimensionPixelSize(R.styleable.TitleBar_titleSize, 18)?.toFloat() ?: 18F
            val titleText = typedArray?.getString(R.styleable.TitleBar_titleDesc)
            typedArray?.recycle()
            textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
            textView!!.setTextColor(titleColor)
            textView!!.text = titleText
        } else {
            textView!!.textSize = 18F
            textView!!.setTextColor(ContextCompat.getColor(context, R.color.color_white))
        }

    }

    fun setText(text: CharSequence) {
        textView?.text = text
    }

}
package com.lpen.jetpack.base.binding

import androidx.databinding.BindingAdapter
import com.lpen.jetpack.widget.TitleBar

/**
 * @author LPen
 * @date 19-3-16
 */
object TitleBarBindingAdapter {

    @BindingAdapter(value = ["titleDesc"], requireAll = false)
    @JvmStatic
    fun onTitleBinding(titleBar: TitleBar, title: CharSequence?) {
        title?.let {
            titleBar.setText(it)
        }
    }
}
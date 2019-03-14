package com.lpen.jetpack.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

/**
 * @author LPen
 * @date 19-3-10
 */
object TipsUtil {

    private var mToast: Toast? = null

    @SuppressLint("ShowToast")
    fun toastShortMsg(context: Context, msg: String?) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg ?: "", Toast.LENGTH_SHORT)
        }
        mToast!!.setText(msg ?: "")
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.show()
    }

    @SuppressLint("ShowToast")
    fun toastLongMsg(context: Context, msg: String?) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg ?: "", Toast.LENGTH_LONG)
        }
        mToast!!.setText(msg ?: "")
        mToast!!.duration = Toast.LENGTH_LONG
        mToast!!.show()
    }

}
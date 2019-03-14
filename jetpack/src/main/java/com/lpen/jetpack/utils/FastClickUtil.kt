package com.lpen.jetpack.utils

/**
 * @author LPen
 * @date 19-3-10
 */
object FastClickUtil {

    var mLastClickTime: Long = 0

    fun isFastClick(): Boolean {
        return isFastClick(500)
    }

    fun isFastClick(interval: Int): Boolean {
        if (System.currentTimeMillis() - mLastClickTime > interval) {
            mLastClickTime = System.currentTimeMillis()
            return false
        }
        return true
    }

}
package com.lpen.jetpack.base

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author LPen
 * @date 19-3-10
 *
 * @param hDistance 横向的Item间隔距离
 * @param vDistance 纵向的Item间隔距离
 * @param spanCount 展示的列数
 * @param needBlankTB 第一排和最后一排的上下是否要留白
 */
class DividerDecoration(private val hDistance: Int, private val vDistance: Int, private val spanCount: Int, private val needBlankTB: Boolean = false) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val curPos = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition    // parent.indexOfChild(view)

        if (spanCount == 1) {
            outRect.left = hDistance
            outRect.right = hDistance
            if (needBlankTB) {
                when (curPos) {
                    0 -> {
                        outRect.top = vDistance
                        outRect.bottom = vDistance / 2
                    }
                    parent.childCount - 1 -> {
                        outRect.top = vDistance / 2
                        outRect.bottom = vDistance
                    }
                    else -> {
                        outRect.top = vDistance / 2
                        outRect.bottom = vDistance / 2
                    }
                }
            } else {
                when (curPos) {
                    0 -> {
                        outRect.top = 0
                        outRect.bottom = vDistance / 2
                    }
                    parent.childCount - 1 -> {
                        outRect.top = vDistance / 2
                        outRect.bottom = 0
                    }
                    else -> {
                        outRect.top = vDistance / 2
                        outRect.bottom = vDistance / 2
                    }
                }
            }
        } else if (spanCount > 1) {
            when (curPos % spanCount) {
                0 -> {  // 左边
                    outRect.left = hDistance
                    outRect.right = hDistance / 2
                }
                spanCount - 1 -> {  // 右边
                    outRect.left = hDistance / 2
                    outRect.right = hDistance
                }
                else -> {   // 居中
                    outRect.left = hDistance / 2
                    outRect.right = hDistance / 2
                }
            }
            if (needBlankTB) {
                when (curPos / spanCount) {
                    0 -> {  // 第一排
                        outRect.top = vDistance
                        outRect.bottom = vDistance / 2
                    }
                    (parent.adapter?.itemCount ?: 0) / spanCount - 1 -> {   // 最后一排
                        outRect.top = vDistance / 2
                        outRect.bottom = vDistance
                    }
                    else -> {   // 中间
                        outRect.top = vDistance / 2
                        outRect.bottom = vDistance / 2
                    }
                }
            } else {
                when (curPos / spanCount) {
                    0 -> {  // 第一排
                        outRect.top = 0
                        outRect.bottom = vDistance / 2
                    }
                    (parent.adapter?.itemCount ?: 0) / spanCount - 1 -> {   // 最后一排
                        outRect.top = vDistance / 2
                        outRect.bottom = 0
                    }
                    else -> {   // 中间
                        outRect.top = vDistance / 2
                        outRect.bottom = vDistance / 2
                    }
                }
            }
        }
    }

}
package com.lpen.jetpack.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lpen.jetpack.R
import com.lpen.jetpack.databinding.ActivityBasicBinding
import com.lpen.jetpack.ui.fragment.IndexFragment
import com.lpen.jetpack.ui.itemviewmodel.IndexItemViewModel
import com.lpen.jetpack.ui.listener.OnItemClickCallback
import com.lpen.jetpack.utils.TipsUtil

/**
 * @author LPen
 * @date 19-3-10
 */
class BasicActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBasicBinding>(this, R.layout.activity_basic)

        val list = ArrayList<IndexItemViewModel>()
        list.add(IndexItemViewModel("AppCompat", "在较低版本的 Android 系统上恰当地降级"))
        list.add(IndexItemViewModel("Android KTX", "编写更简洁、惯用的 Kotlin 代码"))
        list.add(IndexItemViewModel("多 dex 处理", "为具有多个 DEX 文件的应用提供支持"))
        list.add(IndexItemViewModel("测试", "用于单元和运行时界面测试的 Android 测试框架"))

        (supportFragmentManager.findFragmentById(R.id.fragment_archIndex) as IndexFragment).apply {
            setIndexList(list)
            addOnItemClickListener(object : OnItemClickCallback<Int> {
                override fun onItemClick(t: Int) {
                    TipsUtil.toastShortMsg(this@BasicActivity, list[t].title.get())
                }
            })
        }
    }

}
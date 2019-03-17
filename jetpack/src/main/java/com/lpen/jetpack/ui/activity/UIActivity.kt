package com.lpen.jetpack.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lpen.jetpack.R
import com.lpen.jetpack.databinding.ActivityUiBinding
import com.lpen.jetpack.ui.fragment.IndexFragment
import com.lpen.jetpack.ui.itemviewmodel.IndexItemViewModel
import com.lpen.jetpack.ui.listener.OnItemClickCallback
import com.lpen.jetpack.utils.TipsUtil

/**
 * @author LPen
 * @date 19-3-10
 */
class UIActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityUiBinding>(this, R.layout.activity_ui)

        val list = ArrayList<IndexItemViewModel>()
        list.add(IndexItemViewModel("动画和过渡", "移动微件和在屏幕之间过渡"))
        list.add(IndexItemViewModel("Auto", "有助于开发 Android Auto 应用的组件"))
        list.add(IndexItemViewModel("表情符号", "在旧版平台上启用最新的表情符号字体"))
        list.add(IndexItemViewModel("Fragment", "组件化界面的基本单位"))
        list.add(IndexItemViewModel("布局", "使用不同的算法布置微件"))
        list.add(IndexItemViewModel("调色板", "从调色板中提取出有用的信息"))
        list.add(IndexItemViewModel("TV", "有助于开发 Android TV 应用的组件"))
        list.add(IndexItemViewModel("Wear OS by Google", "有助于开发 Wear 应用的组件"))

        (supportFragmentManager.findFragmentById(R.id.fragment_archIndex) as IndexFragment).apply {
            setIndexList(list)
            addOnItemClickListener(object : OnItemClickCallback<Int> {
                override fun onItemClick(t: Int) {
                    TipsUtil.toastShortMsg(this@UIActivity, list[t].title.get())
                }
            })
        }
    }

}
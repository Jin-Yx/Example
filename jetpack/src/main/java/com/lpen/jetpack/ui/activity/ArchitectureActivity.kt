package com.lpen.jetpack.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lpen.jetpack.R
import com.lpen.jetpack.databinding.ActivityArchBinding
import com.lpen.jetpack.ui.fragment.IndexFragment
import com.lpen.jetpack.ui.itemviewmodel.IndexItemViewModel
import com.lpen.jetpack.ui.listener.OnItemClickCallback
import com.lpen.jetpack.utils.TipsUtil

/**
 * @author LPen
 * @date 19-3-10
 */
class ArchitectureActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityArchBinding>(this, R.layout.activity_arch)

        val list = ArrayList<IndexItemViewModel>()
        list.add(IndexItemViewModel("数据绑定", "以声明方式将可观察数据绑定到界面元素"))
        list.add(IndexItemViewModel("Lifecycles", "管理您的 Activity 和 Fragment 生命周期"))
        list.add(IndexItemViewModel("LiveData", "在底层数据库更改时通知视图"))
        list.add(IndexItemViewModel("Navigation", "处理应用内导航所需的一切"))
        list.add(IndexItemViewModel("Paging", "逐步从您的数据源按需加载信息"))
        list.add(IndexItemViewModel("Room", "流畅地访问 SQLite 数据库"))
        list.add(IndexItemViewModel("ViewModel", "以注重生命周期的方式管理界面相关的数据"))
        list.add(IndexItemViewModel("WorkManager", "管理您的 Android 后台作业"))

        (supportFragmentManager.findFragmentById(R.id.fragment_archIndex) as IndexFragment).apply {
            setIndexList(list)
            addOnItemClickListener(object : OnItemClickCallback<Int> {
                override fun onItemClick(t: Int) {
                    TipsUtil.toastShortMsg(this@ArchitectureActivity, list[t].title.get())
                }
            })
        }
    }

}
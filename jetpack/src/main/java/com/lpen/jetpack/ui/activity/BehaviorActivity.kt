package com.lpen.jetpack.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lpen.jetpack.R
import com.lpen.jetpack.databinding.ActivityBehaviorBinding
import com.lpen.jetpack.ui.fragment.IndexFragment
import com.lpen.jetpack.ui.itemviewmodel.IndexItemViewModel
import com.lpen.jetpack.ui.listener.OnItemClickCallback
import com.lpen.jetpack.utils.TipsUtil

/**
 * @author LPen
 * @date 19-3-10
 */
class BehaviorActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBehaviorBinding>(this, R.layout.activity_behavior)

        val list = ArrayList<IndexItemViewModel>()
        list.add(IndexItemViewModel("下载管理器", "安排和管理大量下载任务"))
        list.add(IndexItemViewModel("媒体和播放", "用于媒体播放和路由的向后兼容 API（包括 Google Cast）"))
        list.add(IndexItemViewModel("通知", "提供向后兼容的通知 API，支持 Wear 和 Auto"))
        list.add(IndexItemViewModel("权限", "用于检查和请求应用权限的兼容性 API"))
        list.add(IndexItemViewModel("偏好设置", "创建交互式设置屏幕"))
        list.add(IndexItemViewModel("共享", "提供适合应用操作栏的共享操作"))
        list.add(IndexItemViewModel("切片", "创建可在应用外部显示应用数据的灵活界面元素"))

        (supportFragmentManager.findFragmentById(R.id.fragment_behaviorIndex) as IndexFragment).apply {
            setIndexList(list)
            addOnItemClickListener(object : OnItemClickCallback<Int> {
                override fun onItemClick(t: Int) {
                    TipsUtil.toastShortMsg(this@BehaviorActivity, list[t].title.get())
                }
            })
        }
    }

}
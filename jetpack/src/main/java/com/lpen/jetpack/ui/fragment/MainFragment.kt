package com.lpen.jetpack.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.lpen.jetpack.R
import com.lpen.jetpack.base.DividerDecoration
import com.lpen.jetpack.databinding.MainFragmentBinding
import com.lpen.jetpack.ui.activity.ArchitectureActivity
import com.lpen.jetpack.ui.activity.BasicActivity
import com.lpen.jetpack.ui.activity.BehaviorActivity
import com.lpen.jetpack.ui.activity.UIActivity
import com.lpen.jetpack.ui.adapter.MainAdapter
import com.lpen.jetpack.ui.itemviewmodel.MainItemViewModel
import com.lpen.jetpack.ui.listener.OnItemClickCallback
import com.lpen.jetpack.ui.viewmodel.MainViewModel
import com.lpen.jetpack.utils.FastClickUtil
import com.lpen.jetpack.utils.TipsUtil
import kotlinx.android.synthetic.main.main_fragment.*

/**
 * @author LPen
 * @Date 2019/03/09
 */
class MainFragment : Fragment() {

    private lateinit var mBinding: MainFragmentBinding
    private lateinit var mAdapter: MainAdapter

    private val list = listOf("基础", "架构", "行为", "界面")

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        mBinding.executePendingBindings()
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mBinding.viewModel = viewModel
        mBinding.setLifecycleOwner(this)

        recyclerMain.layoutManager = GridLayoutManager(activity, 2)
        recyclerMain.addItemDecoration(DividerDecoration(16, 16, 2, true))
        recyclerMain.postDelayed({
            val height = recyclerMain.measuredHeight
            mAdapter = MainAdapter(height - 48)
            mAdapter.addOnItemClickListener(object : OnItemClickCallback<MainItemViewModel> {
                override fun onItemClick(model: MainItemViewModel) {
                    if (FastClickUtil.isFastClick()) return
                    TipsUtil.toastShortMsg(activity!!, model.label.get())
                    val index = list.indexOf(model.label.get())
                    when(index) {
                        0 -> { startActivity(Intent(activity, BasicActivity::class.java)) }
                        1 -> { startActivity(Intent(activity, ArchitectureActivity::class.java)) }
                        2 -> { startActivity(Intent(activity, BehaviorActivity::class.java)) }
                        3 -> { startActivity(Intent(activity, UIActivity::class.java)) }
                    }
                }
            })
            recyclerMain.adapter = mAdapter

            initData()
        }, 0)
    }

    private fun initData() {
        val models = ArrayList<MainItemViewModel>()
        for (index in list.indices) {
            val itemModel = MainItemViewModel()
            itemModel.label.set(list[index])
            when (index) {
                0 -> itemModel.backColor.set(ContextCompat.getColor(activity!!, R.color.color_0e738e))
                1 -> itemModel.backColor.set(ContextCompat.getColor(activity!!, R.color.color_7a0b1d))
                2 -> itemModel.backColor.set(ContextCompat.getColor(activity!!, R.color.color_432c7d))
                3 -> itemModel.backColor.set(ContextCompat.getColor(activity!!, R.color.color_1863a9))
            }
            models.add(itemModel)
        }
        mAdapter.addData(models)
    }
}

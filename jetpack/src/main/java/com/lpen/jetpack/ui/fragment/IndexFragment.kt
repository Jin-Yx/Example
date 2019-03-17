package com.lpen.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lpen.jetpack.R
import com.lpen.jetpack.base.DividerDecoration
import com.lpen.jetpack.databinding.FragmentIndexBinding
import com.lpen.jetpack.ui.activity.ArchitectureActivity
import com.lpen.jetpack.ui.activity.BasicActivity
import com.lpen.jetpack.ui.activity.BehaviorActivity
import com.lpen.jetpack.ui.activity.UIActivity
import com.lpen.jetpack.ui.adapter.IndexAdapter
import com.lpen.jetpack.ui.itemviewmodel.IndexItemViewModel
import com.lpen.jetpack.ui.listener.OnItemClickCallback
import com.lpen.jetpack.ui.viewmodel.IndexViewModel
import kotlinx.android.synthetic.main.fragment_index.*

/**
 * @author LPen
 * @date 19-3-14
 */
class IndexFragment : Fragment() {

    private lateinit var mBinding: FragmentIndexBinding
    private var mAdapter: IndexAdapter? = null
    private var mListener: OnItemClickCallback<Int>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_index, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = IndexViewModel()
        mBinding.viewModel = viewModel

        when (activity) {
            is BasicActivity -> viewModel.title.set("基础组件")
            is ArchitectureActivity -> viewModel.title.set("架构组件")
            is BehaviorActivity -> viewModel.title.set("行为组件")
            is UIActivity -> viewModel.title.set("界面组件")
        }

        recyclerIndex.layoutManager = LinearLayoutManager(activity)
        recyclerIndex.addItemDecoration(DividerDecoration(0, 16, 1, true))
        mAdapter = IndexAdapter()
        recyclerIndex.adapter = mAdapter

        mAdapter!!.setOnItemClickListener { _, _, position ->
            mListener?.onItemClick(position)
        }
    }

    fun setIndexList(modelList: List<IndexItemViewModel>) {
        mAdapter?.setNewData(modelList)
    }

    fun addOnItemClickListener(listener: OnItemClickCallback<Int>?) {
        mListener = listener
    }

}
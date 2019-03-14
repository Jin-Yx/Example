package com.lpen.jetpack.ui.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lpen.jetpack.R
import com.lpen.jetpack.databinding.ItemMainBinding
import com.lpen.jetpack.ui.itemviewmodel.MainItemViewModel
import com.lpen.jetpack.ui.listener.OnItemClickCallback

/**
 * @author LPen
 * @date 19-3-10
 */
class MainAdapter(private val height: Int): BaseQuickAdapter<MainItemViewModel, BaseViewHolder>(R.layout.item_main) {

    private var mListener: OnItemClickCallback<MainItemViewModel>? = null

    override fun convert(helper: BaseViewHolder?, item: MainItemViewModel?) {
        val binding = DataBindingUtil.bind<ItemMainBinding>(helper!!.itemView)
        binding!!.viewModel = item
        if (mListener != null) {
            binding.listener = mListener
        }

        val params = binding.txtLabel.layoutParams
        params.height = height / 2
        binding.txtLabel.layoutParams = params
    }

    fun addOnItemClickListener(listener: OnItemClickCallback<MainItemViewModel>?) {
        mListener = listener
    }

}
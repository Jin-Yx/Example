package com.lpen.jetpack.ui.adapter

import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lpen.jetpack.R
import com.lpen.jetpack.ui.itemviewmodel.IndexItemViewModel
import com.lpen.jetpack.databinding.ItemIndexBinding

/**
 * @author LPen
 * @date 19-3-14
 */
class IndexAdapter: BaseQuickAdapter<IndexItemViewModel, BaseViewHolder>(R.layout.item_index) {

    override fun convert(helper: BaseViewHolder?, item: IndexItemViewModel?) {
        val binding = DataBindingUtil.bind<ItemIndexBinding>(helper?.itemView!!)
        binding!!.executePendingBindings()
        binding.viewModel = item

        val pos = data.indexOf(item)
        when(pos % 4) {
            0 -> helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_3ABF33))
            1 -> helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_E5C110))
            2 -> helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_9029BC))
            else -> helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_BC423E))
        }
    }

}
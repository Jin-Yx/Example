package com.lpen.jetpack.ui.itemviewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * @author LPen
 * @date 19-3-14
 */
class IndexItemViewModel(title: String, desc: String) : ViewModel() {

    constructor(): this("", "")

    val title = ObservableField<String>(title)
    val description = ObservableField<String>(desc)

}
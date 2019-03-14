package com.lpen.jetpack.ui.itemviewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

/**
 * @author LPen
 * @date 19-3-10
 */
class MainItemViewModel: ViewModel() {

    val label = ObservableField<String>()
    val backColor = ObservableInt()

}
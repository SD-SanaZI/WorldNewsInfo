package com.example.worldnewsinfo.presentation.filter

import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes

data class FilterSortButtonModel(
    val filterSort: FilterSort,
    @StringRes val name: Int,
    val viewHolder: View,
    val imageView: ImageView,
)
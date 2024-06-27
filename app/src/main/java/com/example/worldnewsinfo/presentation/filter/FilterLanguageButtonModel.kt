package com.example.worldnewsinfo.presentation.filter

import android.view.View
import androidx.annotation.StringRes

data class FilterLanguageButtonModel(
    val filterType: FilterLanguage,
    @StringRes val name: Int,
    val viewHolder: View,
)
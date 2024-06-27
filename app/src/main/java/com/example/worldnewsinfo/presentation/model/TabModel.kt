package com.example.worldnewsinfo.presentation.model

import androidx.annotation.DrawableRes
import com.example.worldnewsinfo.presentation.headlines.HeadlinesTab

data class TabModel(
    val headlinesTab: HeadlinesTab,
    @DrawableRes val img: Int,
    val text: String,
    val isActive: Boolean
)
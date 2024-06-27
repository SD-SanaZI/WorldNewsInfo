package com.example.worldnewsinfo.presentation.news

import android.text.SpannableString
import androidx.annotation.DrawableRes

data class NewsPageModel(
    val shortText: String,
    val supportingText: String,
    val source: String,
    val text: SpannableString?,
    @DrawableRes val onEmptyImg: Int,
)
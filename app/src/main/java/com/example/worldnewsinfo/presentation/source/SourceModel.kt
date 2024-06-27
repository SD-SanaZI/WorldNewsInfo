package com.example.worldnewsinfo.presentation.source

import androidx.annotation.DrawableRes

data class SourceModel(
    @DrawableRes
    val sourceImg: Int,
    val sourceName: String,
    val sourceId: String,
    val supportingText: String
)
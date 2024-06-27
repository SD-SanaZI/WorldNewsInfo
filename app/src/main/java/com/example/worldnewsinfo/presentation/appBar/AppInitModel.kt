package com.example.worldnewsinfo.presentation.appBar

import java.io.Serializable

data class AppInitModel(
    val viewHeight: Int = 116,
    val backgroundImg: String? = null,
    val mode: Mode = Mode.STANDARD,
    val appBarDrawableRes: AppBarDrawableRes = AppBarDrawableRes(),
    val text: String = "",
) : Serializable
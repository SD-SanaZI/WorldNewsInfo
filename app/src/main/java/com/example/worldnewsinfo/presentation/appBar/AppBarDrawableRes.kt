package com.example.worldnewsinfo.presentation.appBar

import androidx.annotation.DrawableRes
import java.io.Serializable

data class AppBarDrawableRes(
    @DrawableRes val firstDrawableResId: Int? = null,
    @DrawableRes val secondDrawableResId: Int? = null,
    @DrawableRes val penultimateDrawableResId: Int? = null,
    @DrawableRes val lastDrawableResId: Int? = null
): Serializable
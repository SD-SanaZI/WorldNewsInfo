package com.example.worldnewsinfo.application

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import java.io.Serializable

fun <T : View> View.find(@IdRes idRes: Int): Lazy<T> {
    return lazy { findViewById(idRes) }
}

inline fun <reified T : Serializable> getSerializableCompat(bundle: Bundle?, key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle?.getSerializable(key, T::class.java)
    } else {
        bundle?.getSerializable(key) as T?
    }
}
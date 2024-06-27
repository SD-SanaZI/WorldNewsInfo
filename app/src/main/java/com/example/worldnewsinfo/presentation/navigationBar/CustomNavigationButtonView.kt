package com.example.worldnewsinfo.presentation.navigationBar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.application.find

class CustomNavigationButtonView(
    context: Context,
    attr: AttributeSet,
) : ConstraintLayout(context, attr) {
    private val imgContainer by find<View>(R.id.nav_bar_view)
    private val text by find<TextView>(R.id.nav_bar_text)
    private val img by find<ImageView>(R.id.nav_bar_img)

    init {
        inflate(context, R.layout.castom_navigation_button, this)
        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.CustomNavigationButtonView,
            0, 0).apply {

            try {
                text.text = getString(R.styleable.CustomNavigationButtonView_nav_text) ?: ""
                img.setImageDrawable(getDrawable(R.styleable.CustomNavigationButtonView_nav_image))
            } finally {
                recycle()
            }
        }
    }

    fun activate(status: Boolean){
        when(status){
            true -> {
                imgContainer.visibility = View.VISIBLE
                text.visibility = View.VISIBLE
            }
            false -> {
                imgContainer.visibility = View.INVISIBLE
                text.visibility = View.GONE
            }
        }
    }
}
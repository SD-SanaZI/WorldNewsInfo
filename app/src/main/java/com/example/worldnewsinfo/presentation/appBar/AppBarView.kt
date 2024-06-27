package com.example.worldnewsinfo.presentation.appBar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.application.find

class AppBarView(
    context: Context,
    attr: AttributeSet,
) : ConstraintLayout(context, attr) {
    private val appBarBackground by find<ImageView>(R.id.app_bar_background)
    private val appBarButtonFirst by find<ImageView>(R.id.app_bar_button_first)
    private val appBarButtonSecond by find<ImageView>(R.id.app_bar_button_second)
    private val appBarButtonPenultimate by find<ImageView>(R.id.app_bar_button_penultimate)
    private val appBarButtonLast by find<ImageView>(R.id.app_bar_button_last)
    private val appBarEdit by find<EditText>(R.id.app_bar_edit)
    private val appBarText by find<TextView>(R.id.app_bar_text)
    private val appBarBudgeLast by find<TextView>(R.id.app_bar_budge_last)

    init {
        inflate(context, R.layout.fragment_app_bar, this)
    }

    fun initializationView(appInitModel: AppInitModel) {
        layoutParams.height =
            (appInitModel.viewHeight * context.resources.displayMetrics.density).toInt()
        appBarBackground.layoutParams.height = layoutParams.height
        setBackground(appInitModel.backgroundImg)
        when (appInitModel.mode) {
            Mode.STANDARD -> standardMode()
            Mode.FIND -> findMode()
            Mode.FILTER -> filterMode()
        }
        updateUI(appInitModel.appBarDrawableRes)
        setText(appInitModel.text)
    }

    fun setOnClickFun(
        firstFun: () -> Unit = {},
        secondFun: () -> Unit = {},
        penultimateFun: () -> Unit = {},
        lastFun: () -> Unit = {},
    ){
        appBarButtonFirst.setOnClickListener {
            firstFun.invoke()
        }
        appBarButtonSecond.setOnClickListener {
            secondFun.invoke()
        }
        appBarButtonPenultimate.setOnClickListener {
            penultimateFun.invoke()
        }
        appBarButtonLast.setOnClickListener {
            lastFun.invoke()
        }
    }

    fun setOnTextChangedFun(
        onTextChangedFun:(CharSequence?, Int, Int, Int) -> Unit = {_,_,_,_->}
    ){
        appBarEdit.addTextChangedListener(
            onTextChanged = onTextChangedFun
        )
    }

    private fun standardMode() {
        appBarText.visibility = View.VISIBLE
        appBarButtonSecond.visibility = View.VISIBLE
        appBarButtonPenultimate.visibility = View.VISIBLE
        appBarEdit.visibility = View.GONE
    }

    private fun findMode() {
        appBarText.visibility = View.GONE
        appBarButtonSecond.visibility = View.GONE
        appBarButtonPenultimate.visibility = View.GONE
        appBarEdit.visibility = View.VISIBLE
    }

    private fun filterMode() {
        appBarText.visibility = View.VISIBLE
        appBarButtonSecond.visibility = View.GONE
        appBarButtonPenultimate.visibility = View.GONE
        appBarEdit.visibility = View.GONE
    }

    private fun updateUI(
        appBarDrawableRes: AppBarDrawableRes
    ) {
        appBarButtonFirst.setImageDrawable(
            getDrawableOrNull(appBarDrawableRes.firstDrawableResId)
        )
        appBarButtonSecond.setImageDrawable(
            getDrawableOrNull(appBarDrawableRes.secondDrawableResId)
        )
        appBarButtonPenultimate.setImageDrawable(
            getDrawableOrNull(appBarDrawableRes.penultimateDrawableResId)
        )
        appBarButtonLast.setImageDrawable(
            getDrawableOrNull(appBarDrawableRes.lastDrawableResId)
        )
    }

    private fun getDrawableOrNull(@DrawableRes drawableRes: Int?): Drawable? {
        return if (drawableRes != null) {
            AppCompatResources.getDrawable(context, drawableRes)
        } else {
            null
        }
    }

    private fun setText(text: String) {
        appBarText.text = text
    }

    private fun setBackground(url: String? = null) {
        appBarBackground.load(url?:colorPrimaryDrawable()) {
            crossfade(true)
            transformations(RoundedCornersTransformation())
        }
    }

    private fun colorPrimaryDrawable(): ColorDrawable{
        val typedValue = TypedValue()
        context.theme.resolveAttribute(
            com.google.android.material.R.attr.colorPrimary,
            typedValue,
            true
        )
        val color = ResourcesCompat.getColorStateList(resources, typedValue.resourceId, null)
        return ColorDrawable(color!!.defaultColor)
    }

    fun setFilterBudge(num: Int) {
        appBarBudgeLast.text = num.toString()
        appBarBudgeLast.visibility = if (num == 0) View.INVISIBLE else View.VISIBLE
    }
}
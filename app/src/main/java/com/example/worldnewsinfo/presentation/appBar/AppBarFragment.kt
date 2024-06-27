package com.example.worldnewsinfo.presentation.appBar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.worldnewsinfo.application.getSerializableCompat
import com.example.worldnewsinfo.databinding.FragmentAppBarBinding
import com.example.worldnewsinfo.presentation.BaseFragment

const val APP_INIT_MODEL_KEY = "APP_INIT_MODEL_KEY"

//TODO delete
class AppBarFragment : BaseFragment<FragmentAppBarBinding>() {
    private lateinit var appInitModel: AppInitModel

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAppBarBinding {
        return FragmentAppBarBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance(appInitModel: AppInitModel): AppBarFragment {
            val fragment = AppBarFragment()
            val args = Bundle()
            args.putSerializable(APP_INIT_MODEL_KEY, appInitModel)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            appInitModel = getSerializableCompat<AppInitModel>(arguments, APP_INIT_MODEL_KEY) ?: AppInitModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view.layoutParams.height =
            (appInitModel.viewHeight * requireContext().resources.displayMetrics.density).toInt()
        binding.appBarBackground.layoutParams.height = view.layoutParams.height
        setBackground(appInitModel.backgroundImg)
        when (appInitModel.mode) {
            Mode.STANDARD -> standardMode()
            Mode.FIND -> findMode()
            Mode.FILTER -> filterMode()
        }
        updateUI(requireContext(), appInitModel.appBarDrawableRes)
        setText(appInitModel.text)
        return view
    }

    fun setOnClickFun(
        firstFun: () -> Unit = {},
        secondFun: () -> Unit = {},
        penultimateFun: () -> Unit = {},
        lastFun: () -> Unit = {},
        ){
        binding.appBarButtonFirst.setOnClickListener {
            firstFun.invoke()
        }
        binding.appBarButtonSecond.setOnClickListener {
            secondFun.invoke()
        }
        binding.appBarButtonPenultimate.setOnClickListener {
            penultimateFun.invoke()
        }
        binding.appBarButtonLast.setOnClickListener {
            lastFun.invoke()
        }
    }

    fun setOnTextChangedFun(
        onTextChangedFun:(CharSequence?, Int, Int, Int) -> Unit = {_,_,_,_->}
    ){
        binding.appBarEdit.addTextChangedListener(
            onTextChanged = onTextChangedFun
        )
    }

    private fun standardMode() {
        binding.appBarText.visibility = View.VISIBLE
        binding.appBarButtonSecond.visibility = View.VISIBLE
        binding.appBarButtonPenultimate.visibility = View.VISIBLE
        binding.appBarEdit.visibility = View.GONE
    }

    private fun findMode() {
        binding.appBarText.visibility = View.GONE
        binding.appBarButtonSecond.visibility = View.GONE
        binding.appBarButtonPenultimate.visibility = View.GONE
        binding.appBarEdit.visibility = View.VISIBLE
    }

    private fun filterMode() {
        binding.appBarText.visibility = View.VISIBLE
        binding.appBarButtonSecond.visibility = View.GONE
        binding.appBarButtonPenultimate.visibility = View.GONE
        binding.appBarEdit.visibility = View.GONE
    }

    private fun updateUI(
        context: Context,
        appBarDrawableRes: AppBarDrawableRes
    ) {
        binding.appBarButtonFirst.setImageDrawable(
            getDrawableOrNull(appBarDrawableRes.firstDrawableResId, context)
        )
        binding.appBarButtonSecond.setImageDrawable(
            getDrawableOrNull(appBarDrawableRes.secondDrawableResId, context)
        )
        binding.appBarButtonPenultimate.setImageDrawable(
            getDrawableOrNull(
                appBarDrawableRes.penultimateDrawableResId, context
            )
        )
        binding.appBarButtonLast.setImageDrawable(
            getDrawableOrNull(appBarDrawableRes.lastDrawableResId, context)
        )
    }

    private fun getDrawableOrNull(@DrawableRes drawableRes: Int?, context: Context): Drawable? {
        return if (drawableRes != null) {
            AppCompatResources.getDrawable(context, drawableRes)
        } else {
            null
        }
    }

    private fun setText(text: String) {
        binding.appBarText.text = text
    }

    private fun setBackground(url: String? = null) {
        if (url != null) {
            binding.appBarBackground.load(url) {
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }
        } else {
            val a = TypedValue()
            requireContext().theme.resolveAttribute(
                com.google.android.material.R.attr.colorPrimary,
                a,
                true
            )
            val color = ResourcesCompat.getColorStateList(resources, a.resourceId, null)
            binding.appBarBackground.load(ColorDrawable(color!!.defaultColor)) {
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }
        }

    }

    private fun setFilterBadge(num: Int, visibility: Boolean) {
        binding.appBarBudgeLast.text = num.toString()
        binding.appBarBudgeLast.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }
}
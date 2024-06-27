package com.example.worldnewsinfo.presentation.filter

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.databinding.FragmentFilterBinding
import com.example.worldnewsinfo.presentation.BaseFragment
import com.example.worldnewsinfo.presentation.appBar.AppBarDrawableRes
import com.example.worldnewsinfo.presentation.appBar.AppInitModel
import com.example.worldnewsinfo.presentation.appBar.Mode
import com.google.android.material.datepicker.MaterialDatePicker
import org.orbitmvi.orbit.viewmodel.observe
import java.text.DateFormat.getDateInstance
import java.util.Date

class FilterFragment : BaseFragment<FragmentFilterBinding>() {
    private val filterViewModel by lazy { ViewModelProvider(requireActivity())[FilterViewModel::class.java] }
    private lateinit var filterSortButtonsList: List<FilterSortButtonModel>
    private lateinit var filterLanguageButtonsList: List<FilterLanguageButtonModel>

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterBinding {
        return FragmentFilterBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = FilterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        appBarSetting()
        mviConnect()

        filterSortButtonsList = FilterSort.entries.map {
            mapFilterSortButtonModel(it)
        }
        filterLanguageButtonsList = FilterLanguage.entries.map {
            mapFilterLanguageButtonModel(it)
        }

        prepareFilterTypeButtons()
        prepareFilterLanguageButtons()

        binding.filterCalendarImg.setOnClickListener {
            onCalendarImgClick()
        }
        return view
    }

    private fun appBarSetting() {
        val appInitModel = AppInitModel(
            mode = Mode.FILTER,
            appBarDrawableRes = AppBarDrawableRes(
                firstDrawableResId = R.drawable.prev_icon,
                lastDrawableResId = R.drawable.filter_positive_icon
            ),
            text = getString(R.string.filters)
        )
        binding.filterAppBar.initializationView(appInitModel)
        binding.filterAppBar.setOnClickFun(
            firstFun = {
                filterViewModel.clearFilter()
                NavigatorCompat.back()
            },
            lastFun = {
                NavigatorCompat.back()
            },
        )
    }

    private fun mviConnect() {
        filterViewModel.observe(
            lifecycleOwner = this,
            state = ::render,
            sideEffect = ::handleSideEffect
        )
    }

    private fun render(state: FilterState) {
        state.filterSortList.forEach { filterSortModel ->
            val filterSortButtonModel =
                filterSortButtonsList.find { it.filterSort.name == filterSortModel.name }
                    ?: throw Exception("mvi render")
            setFilterButtonState(
                filterSortButtonModel.viewHolder,
                filterSortButtonModel.imageView,
                filterSortModel.state
            )
        }
        if (state.date != null){
            binding.filterChooseDateString.text =
                requireContext().getString(
                        R.string.date_string,
                        getDateInstance().format(state.date.first),
                        getDateInstance().format(state.date.second),
                    )
            binding.filterChooseDateString.setTextColor(getColor(com.google.android.material.R.attr.colorPrimary))
            binding.filterCalendarImgContainer.visibility = View.VISIBLE
            binding.filterCalendarImg.backgroundTintList = getColor(com.google.android.material.R.attr.colorOnPrimary)
        }else{
            binding.filterChooseDateString.text = getString(R.string.choose_date)
            binding.filterChooseDateString.setTextColor(getColor(com.google.android.material.R.attr.colorOutline))
            binding.filterCalendarImgContainer.visibility = View.INVISIBLE
            binding.filterCalendarImg.backgroundTintList = null
        }

        filterLanguageButtonsList.forEach {
            setFilterButtonState(it.viewHolder, null, it.filterType == state.filterLanguage)
        }
    }

    private fun handleSideEffect(sideEffect: FilterSideEffect) {
        when (sideEffect) {
            else -> {}
        }
    }

    private fun onCalendarImgClick() {
        val datePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.Theme_Material3)
                .setTitleText("Select date")
                .build()
        datePicker.addOnPositiveButtonClickListener {
            onDataPickerPositiveButtonClick(it)
        }
        datePicker.addOnNegativeButtonClickListener {
            filterViewModel.setFilterDate(null)
        }
        datePicker.show(parentFragmentManager, "")
    }

    private fun onDataPickerPositiveButtonClick(date: androidx.core.util.Pair<Long, Long>) {
        filterViewModel.setFilterDate(Pair(Date(date.first), Date(date.second)))
    }

    private fun prepareFilterTypeButtons() {
        filterSortButtonsList.forEach { filterTypeButtonModel ->
            filterTypeButtonModel.viewHolder.setOnClickListener {
                onSortButtonClick(it)
            }
        }
    }

    private fun prepareFilterLanguageButtons() {
        filterLanguageButtonsList.forEach { filterLanguageButtonModel ->
            filterLanguageButtonModel.viewHolder.setOnClickListener {
                filterViewModel.setFilterLanguage(filterLanguageButtonModel.filterType)
            }
        }
    }

    private fun getColor(resId: Int): ColorStateList? {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(
            resId,
            typedValue,
            true
        )
        return ResourcesCompat.getColorStateList(resources, typedValue.resourceId, null)
    }

    private fun setFilterButtonColor(view: View, state: Boolean) {
        val color = getColor(com.google.android.material.R.attr.colorSecondaryContainer)
        (view.background as GradientDrawable).color =
            if (state)
                color
            else
                null
    }

    private fun setFilterButtonState(button: View, activeImg: ImageView?, state: Boolean) {
        setFilterButtonColor(button, state)
        activeImg?.visibility =
            if (state) View.VISIBLE
            else View.GONE
    }

    private fun onSortButtonClick(
        viewHolder: View,
    ) {
        val filterSortButtonModel = filterSortButtonsList.find { filterSortButtonModel ->
            filterSortButtonModel.viewHolder == viewHolder
        }!!
        filterViewModel.setFilterSort(filterSortButtonModel.filterSort)
    }

    private fun mapFilterSortButtonModel(filterSort: FilterSort): FilterSortButtonModel {
        return when (filterSort) {
            FilterSort.POPULAR -> FilterSortButtonModel(
                filterSort,
                R.string.popular,
                binding.filterPopularButton,
                binding.filterPopularImg,
            )

            FilterSort.NEW -> FilterSortButtonModel(
                filterSort,
                R.string.new_string,
                binding.filterNewButton,
                binding.filterNewImg,
            )

            FilterSort.RELEVANT -> FilterSortButtonModel(
                filterSort,
                R.string.relevant,
                binding.filterRelevantButton,
                binding.filterRelevantImg,
            )
        }
    }

    private fun mapFilterLanguageButtonModel(filterLanguage: FilterLanguage): FilterLanguageButtonModel {
        return when (filterLanguage) {
            FilterLanguage.RUS -> FilterLanguageButtonModel(
                filterLanguage,
                R.string.language_1,
                binding.filterLanguage1
            )

            FilterLanguage.ENG -> FilterLanguageButtonModel(
                filterLanguage,
                R.string.language_2,
                binding.filterLanguage2
            )

            FilterLanguage.GER -> FilterLanguageButtonModel(
                filterLanguage,
                R.string.language_2,
                binding.filterLanguage3
            )
        }
    }
}


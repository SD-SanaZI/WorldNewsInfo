package com.example.worldnewsinfo.presentation.filter

import java.util.Date

data class FilterState(
    val filterSortList: List<FilterSortModel>,
    val date: Pair<Date, Date>?,
    val filterLanguage: FilterLanguage?
)
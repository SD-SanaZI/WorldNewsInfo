package com.example.worldnewsinfo.presentation.filter

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.util.Date

class FilterViewModel() : ContainerHost<FilterState, FilterSideEffect>, ViewModel() {
    override val container = container<FilterState, FilterSideEffect>(getClearFilter())

    fun setFilterSort(filterSort: FilterSort) = intent {
        reduce {
            val newList = state.filterSortList.toMutableList()
            val index = newList.indexOfFirst {
                it.name == filterSort.name
            }
            if (index != -1) {
                newList[index] = newList[index].copy(
                    state = !newList[index].state
                )
            }
            state.copy(filterSortList = newList)
        }
    }

    fun setFilterLanguage(filterLanguage: FilterLanguage) = intent {
        reduce{
            state.copy(filterLanguage = filterLanguage)
        }
    }

    fun setFilterDate(datePair: Pair<Date,Date>?) = intent {
        reduce {
            state.copy(date = datePair)
        }
    }

    fun clearFilter() = intent{
        reduce {
            getClearFilter()
        }
    }

    private fun getClearFilter():FilterState{
        return FilterState(
            FilterSort.entries.map {
                FilterSortModel(it.name, false)
            },
            null,
            null
        )
    }

    fun getFilterCount():Int{
        var count = 0
        if(container.stateFlow.value.filterSortList.find { it.state } != null) count++
        if(container.stateFlow.value.date != null) count++
        if (container.stateFlow.value.filterLanguage != null) count++
        return count
    }

    fun getFilter():FilterState{
        return container.stateFlow.value
    }
}


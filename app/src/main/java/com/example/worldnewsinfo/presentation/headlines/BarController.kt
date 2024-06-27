package com.example.worldnewsinfo.presentation.headlines

class BarController(private var deactivateFun: () -> Unit) {
    fun changeFilterButton(
        newDeactivateFun: () -> Unit,
        activateFun: () -> Unit
    ) {
        deactivateFun.invoke()
        activateFilterButton(newDeactivateFun, activateFun)
    }

    private fun activateFilterButton(
        newDeactivateFun: () -> Unit,
        activateFun: () -> Unit
    ) {
        deactivateFun = { newDeactivateFun.invoke() }
        activateFun.invoke()
    }

    fun setDeactivateFun(newDeactivateFun: () -> Unit) {
        deactivateFun = { newDeactivateFun.invoke() }
    }
}
package com.example.worldnewsinfo.application

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.worldnewsinfo.R

object NavigatorCompat{
    var activity: AppCompatActivity? = null

    fun goTo(fragment: Fragment, tag: String) {
        activity?.let {
            val findFragment = it.supportFragmentManager.findFragmentByTag(tag)
            if (findFragment == null)
                nextFragmentTransaction(fragment, tag)
            else
                it.supportFragmentManager.popBackStack(tag, 0)
        }
    }

    fun back(){
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun nextFragmentTransaction(fragment: Fragment, tag: String) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main, fragment)
            ?.addToBackStack(tag)?.commit()
    }
}
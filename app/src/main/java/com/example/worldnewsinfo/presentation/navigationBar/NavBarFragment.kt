package com.example.worldnewsinfo.presentation.navigationBar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.worldnewsinfo.presentation.view.MainViewModel
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.databinding.FragmentNavigationBarBinding
import com.example.worldnewsinfo.presentation.BaseFragment
import com.example.worldnewsinfo.presentation.headlines.HeadlinesFragment
import com.example.worldnewsinfo.presentation.saved.SavedFragment
import com.example.worldnewsinfo.presentation.source.SourceFragment
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class NavBarFragment  : BaseFragment<FragmentNavigationBarBinding>() {
    private lateinit var activateButton: CustomNavigationButtonView
    private val activityViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }
    lateinit var disposable: Disposable

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNavigationBarBinding {
        return FragmentNavigationBarBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = NavBarFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        activateButton = binding.customHeadlines
        activateButton.activate(true)
        val observer: Observer<NavBarState> = object : Observer<NavBarState> {
            override fun onNext(t: NavBarState) {
                when(t){
                    NavBarState.HEADLINES -> {
                        changeActiveButtonTo(binding.customHeadlines)
                    }
                    NavBarState.SAVED -> {
                        changeActiveButtonTo(binding.customSaved)
                    }
                    NavBarState.SOURCES -> {
                        changeActiveButtonTo(binding.customSources)
                    }
                }
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onError(e: Throwable) {
                Log.d("Observer<NavBarState>","onError: $e")
            }

            override fun onComplete() {}
        }
        activityViewModel.navBarSubscribe(observer)
        setOnClickListeners()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    private fun setOnClickListeners(){
        binding.customHeadlines.setOnClickListener {
            activityViewModel.navBarState = NavBarState.HEADLINES
            NavigatorCompat.goTo(HeadlinesFragment.newInstance(),activityViewModel.navBarState.toString())
        }
        binding.customSaved.setOnClickListener {
            activityViewModel.navBarState = NavBarState.SAVED
            NavigatorCompat.goTo(SavedFragment.newInstance(),activityViewModel.navBarState.toString())
        }
        binding.customSources.setOnClickListener {
            activityViewModel.navBarState = NavBarState.SOURCES
            NavigatorCompat.goTo(SourceFragment.newInstance(),activityViewModel.navBarState.toString())
        }
    }

    private fun changeActiveButtonTo(button: CustomNavigationButtonView){
        activateButton.activate(false)
        button.activate(true)
        activateButton = button
    }
}
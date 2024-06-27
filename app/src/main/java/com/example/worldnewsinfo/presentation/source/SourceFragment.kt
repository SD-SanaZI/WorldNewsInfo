package com.example.worldnewsinfo.presentation.source

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.worldnewsinfo.presentation.view.MainViewModel
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.databinding.FragmentRecyclerBinding
import com.example.worldnewsinfo.domain.LoadSourcesUseCase
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.domain.SourceParametersDomainModel
import com.example.worldnewsinfo.presentation.BaseFragment
import com.example.worldnewsinfo.presentation.filter.FilterFragment
import com.example.worldnewsinfo.presentation.mapper.SourceModelMapper
import com.example.worldnewsinfo.presentation.appBar.AppBarDrawableRes
import com.example.worldnewsinfo.presentation.appBar.AppInitModel
import com.example.worldnewsinfo.presentation.di.MainComponent
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

class SourceFragment : BaseFragment<FragmentRecyclerBinding>() {
    private val activityViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }

    @Inject
    lateinit var repositoryImpl: Repository

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = SourceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        MainComponent.create(requireContext()).inject(this)
        val repository: Repository = repositoryImpl
        appBarSetting()
        lifecycleScope.launch() {
            val list = LoadSourcesUseCase(
                repository,
                SourceParametersDomainModel()
            )
                .invoke()
                .map { SourceModelMapper().mapSourceDomainModel(it) }
            val adapter = SourceAdapter()
            adapter.submitList(list)
            binding.mainRecycler.adapter = adapter
        }

        return view
    }

    private fun appBarSetting(){
        val appInitModel = AppInitModel(
            appBarDrawableRes = AppBarDrawableRes(
                penultimateDrawableResId = R.drawable.find_icon,
                lastDrawableResId = R.drawable.filter_icon
            ),
            text = getString(R.string.sources_string)
        )
        binding.mainAppBar.initializationView(appInitModel)
        binding.mainAppBar.setOnClickFun(
            lastFun = {
                NavigatorCompat.goTo(
                    FilterFragment.newInstance(),
                    activityViewModel.navBarState.toString()
                )
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.coroutineContext.cancelChildren()
    }
}
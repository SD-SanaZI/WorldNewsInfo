package com.example.worldnewsinfo.presentation.saved

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
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.presentation.BaseFragment
import com.example.worldnewsinfo.presentation.appBar.AppBarDrawableRes
import com.example.worldnewsinfo.presentation.appBar.AppInitModel
import com.example.worldnewsinfo.presentation.appBar.Mode
import com.example.worldnewsinfo.presentation.di.MainComponent
import com.example.worldnewsinfo.presentation.filter.FilterFragment
import com.example.worldnewsinfo.presentation.headlines.NewsAdapter
import com.example.worldnewsinfo.presentation.mapper.NewsModelMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedFragment : BaseFragment<FragmentRecyclerBinding>() {
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
        fun newInstance() = SavedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        MainComponent.create(requireContext()).inject(this)

        val repository: Repository = repositoryImpl

        lifecycleScope.launch {
            val list = repository.savedNews().map { NewsModelMapper().mapNewsDomainModel(it) }
            val adapter = NewsAdapter()
            adapter.submitList(list)
            binding.mainRecycler.adapter = adapter
        }

        val appInitModel = AppInitModel(
            mode = Mode.STANDARD,
            appBarDrawableRes = AppBarDrawableRes(
                penultimateDrawableResId =  R.drawable.find_icon,
                lastDrawableResId = R.drawable.filter_icon
            ),
            text = getString(R.string.saved)
        )
        binding.mainAppBar.initializationView(appInitModel)
        binding.mainAppBar.setOnClickFun(
            penultimateFun = {
                //TODO поиск в бэке
            },
            lastFun = {
                NavigatorCompat.goTo(
                    FilterFragment.newInstance(),
                    activityViewModel.navBarState.toString()
                )
            }
        )
        return view
    }
}
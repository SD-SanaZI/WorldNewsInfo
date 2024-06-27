package com.example.worldnewsinfo.presentation.sourseNews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.worldnewsinfo.presentation.view.MainViewModel
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.databinding.FragmentRecyclerBinding
import com.example.worldnewsinfo.domain.LoadNewsFromEverythingUseCase
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.presentation.BaseFragment
import com.example.worldnewsinfo.presentation.appBar.AppBarDrawableRes
import com.example.worldnewsinfo.presentation.appBar.AppInitModel
import com.example.worldnewsinfo.presentation.appBar.Mode
import com.example.worldnewsinfo.presentation.di.MainComponent
import com.example.worldnewsinfo.presentation.filter.FilterFragment
import com.example.worldnewsinfo.presentation.filter.FilterViewModel
import com.example.worldnewsinfo.presentation.find.FindFragment
import com.example.worldnewsinfo.presentation.headlines.NewsAdapter
import com.example.worldnewsinfo.presentation.mapper.NewsModelMapper
import com.example.worldnewsinfo.presentation.mapper.NewsParametersDomainModelMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SOURCE_STRING_ID_KEY = "SOURCE_STRING_ID_KEY"
const val SOURCE_STRING_NAME_KEY = "SOURCE_STRING_NAME_KEY"

class SourceNewsFragment: BaseFragment<FragmentRecyclerBinding>() {
    private val activityViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }
    private val filterViewModel by lazy { ViewModelProvider(requireActivity())[FilterViewModel::class.java] }
    @Inject
    lateinit var repositoryImpl: Repository
    private lateinit var sourceId: String
    lateinit var sourceName: String

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance(sourceId: String, sourceName: String):SourceNewsFragment{
            val fragment = SourceNewsFragment()
            val args = Bundle()
            args.putString(SOURCE_STRING_ID_KEY, sourceId)
            args.putString(SOURCE_STRING_NAME_KEY, sourceName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            sourceId = requireArguments().getString(SOURCE_STRING_ID_KEY)
                ?: throw Exception("SourceNewsFragment: Wrong init arguments")
            sourceName = requireArguments().getString(SOURCE_STRING_NAME_KEY)
                ?: throw Exception("SourceNewsFragment: Wrong init arguments")
        }
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
            val filter = filterViewModel.getFilter()
            val newsList =LoadNewsFromEverythingUseCase(
                repository,
                NewsParametersDomainModelMapper()
                    .mapNewsParameters(filter)
                    .copy(sources = sourceId)
            ).invoke()
                .map { NewsModelMapper().mapNewsDomainModel(it) }

            val adapter = NewsAdapter()
            adapter.submitList(newsList)
            binding.mainRecycler.adapter = adapter
        }

        appBarSetting()
        return view
    }

    private fun appBarSetting(){
        val appInitModel = AppInitModel(
            mode = Mode.STANDARD,
            appBarDrawableRes = AppBarDrawableRes(
                firstDrawableResId = com.example.worldnewsinfo.R.drawable.prev_icon,
                penultimateDrawableResId =  com.example.worldnewsinfo.R.drawable.find_icon,
                lastDrawableResId = com.example.worldnewsinfo.R.drawable.filter_icon
            ),
            text = sourceName
        )
        binding.mainAppBar.initializationView(appInitModel)
        binding.mainAppBar.setOnClickFun(
            firstFun = {
                NavigatorCompat.back()
            },
            penultimateFun = {
                NavigatorCompat.goTo(
                    FindFragment.newInstance(sourceId),
                    activityViewModel.navBarState.toString()
                )
            },
            lastFun = {
                NavigatorCompat.goTo(
                    FilterFragment.newInstance(),
                    activityViewModel.navBarState.toString()
                )
            }
        )
    }
}
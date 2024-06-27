package com.example.worldnewsinfo.presentation.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.databinding.FragmentRecyclerBinding
import com.example.worldnewsinfo.domain.LoadNewsFromEverythingUseCase
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.presentation.BaseFragment
import com.example.worldnewsinfo.presentation.headlines.NewsAdapter
import com.example.worldnewsinfo.presentation.mapper.NewsModelMapper
import com.example.worldnewsinfo.presentation.appBar.AppBarDrawableRes
import com.example.worldnewsinfo.presentation.appBar.AppInitModel
import com.example.worldnewsinfo.presentation.appBar.Mode
import com.example.worldnewsinfo.presentation.di.MainComponent
import com.example.worldnewsinfo.presentation.filter.FilterViewModel
import com.example.worldnewsinfo.presentation.mapper.NewsParametersDomainModelMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SOURCE_STRING_KEY = "SOURCE_STRING_KEY"

class FindFragment : BaseFragment<FragmentRecyclerBinding>() {
    private val filterViewModel by lazy { ViewModelProvider(requireActivity())[FilterViewModel::class.java] }

    @Inject
    lateinit var repositoryImpl: Repository
    var source: String? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = FindFragment()
        fun newInstance(source: String): FindFragment {
            val fragment = FindFragment()
            val args = Bundle()
            args.putString(SOURCE_STRING_KEY, source)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            source = requireArguments().getString(SOURCE_STRING_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        MainComponent.create(requireContext()).inject(this)
        val adapter = NewsAdapter(com.google.android.material.R.attr.colorPrimary)
        adapter.submitList(listOf())
        appBarSetting(adapter)
        binding.mainRecycler.adapter = adapter
        return view
    }

    private fun appBarSetting(adapter: NewsAdapter) {
        val appInitModel = AppInitModel(
            viewHeight = 124,
            mode = Mode.FIND,
            appBarDrawableRes = AppBarDrawableRes(
                firstDrawableResId = R.drawable.prev_icon,
                lastDrawableResId = R.drawable.filter_negative_icon
            ),
        )
        binding.mainAppBar.initializationView(appInitModel)
        binding.mainAppBar.setOnClickFun(
            firstFun = {
                NavigatorCompat.back()
            },
            lastFun = {
                NavigatorCompat.back()
            },
        )
        binding.mainAppBar.setOnTextChangedFun { charSequence, _, _, _ ->
            val filter = filterViewModel.getFilter()
            lifecycleScope.launch() {
                val list = LoadNewsFromEverythingUseCase(
                    repositoryImpl,
                    NewsParametersDomainModelMapper()
                        .mapNewsParameters(filter, charSequence.toString())
                        .copy(sources = source)
                ).invoke().map {
                    NewsModelMapper().mapNewsDomainModel(it)
                }
                adapter.submitList(list)
            }
        }
    }
}
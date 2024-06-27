package com.example.worldnewsinfo.presentation.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldnewsinfo.presentation.view.MainViewModel
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.databinding.FragmentHeadlinesBinding
import com.example.worldnewsinfo.presentation.appBar.AppBarDrawableRes
import com.example.worldnewsinfo.presentation.appBar.AppInitModel
import com.example.worldnewsinfo.presentation.filter.FilterFragment
import com.example.worldnewsinfo.presentation.filter.FilterViewModel
import com.example.worldnewsinfo.presentation.find.FindFragment
import com.example.worldnewsinfo.presentation.model.NewsModel
import com.example.worldnewsinfo.presentation.model.TabModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class HeadlinesFragment : MvpAppCompatFragment(), MainView {
    private val activityViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }
    private val filterViewModel by lazy { ViewModelProvider(requireActivity())[FilterViewModel::class.java] }
    private val presenter: HeadlinesPresenter by moxyPresenter { HeadlinesPresenter() }
    private var _binding: FragmentHeadlinesBinding? = null
    val binding
        get() = requireNotNull(_binding) {
            "Binding isn't init"
        }
    val newsAdapter = NewsAdapter()

    fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHeadlinesBinding {
        return FragmentHeadlinesBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = HeadlinesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createBinding(inflater, container)

        appBarSetting()

        val filterCount = filterViewModel.getFilterCount()
        val filter = filterViewModel.getFilter()
        presenter.setNewsList(filter,requireContext())
        prepareTabRecycle(newsAdapter, filterCount)
        return binding.root
    }

    override fun setRecyclerAdapter(list: Observable<List<NewsModel>>) {
        newsAdapter.endLoading()
        list.subscribe{ v -> newsAdapter.submitList(v) }
        binding.newsRecycler.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = binding.newsRecycler.layoutManager as LinearLayoutManager
                    val visibleItemCount: Int = layoutManager.childCount
                    val totalItemCount: Int = layoutManager.itemCount
                    val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading()) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            newsAdapter.startLoading()
                            val filter = filterViewModel.getFilter()
                            presenter.expandList(filter,requireContext())
                        }
                    }

                }

                fun isLoading(): Boolean{
                    return newsAdapter.getLoading()
                }
            }
        )
        binding.newsRecycler.adapter = newsAdapter
    }

    private fun prepareTabRecycle(newsAdapter: NewsAdapter, filterCount: Int) {
        if (filterCount == 0) {
            binding.tabRecycler.visibility = View.VISIBLE
            val tabController = BarController {}
            val tabList = HeadlinesTab.entries.map { mapTabModel(it) }.toMutableList()
            val tabAdapter = TabAdapter { position ->
                presenter.setActiveHeadlinesTab(tabList[position].headlinesTab)
            }
            val tabObserver = Observer<HeadlinesTab> { headlinesTab ->
                setTabActive(headlinesTab, tabList, tabController)
                lifecycleScope.launch() {
                    tabAdapter.submitList(tabList.toList())
                    val filter = filterViewModel.getFilter()
                    presenter.setNewsList(filter,requireContext())
                }
            }
            presenter.activeHeadlinesTab.observe(viewLifecycleOwner, tabObserver)
            tabAdapter.submitList(tabList.toList())
            binding.tabRecycler.adapter = tabAdapter
        } else {
            binding.tabRecycler.visibility = View.GONE
        }
    }

    private fun appBarSetting() {
        val appInitModel = AppInitModel(
            appBarDrawableRes = AppBarDrawableRes(
                penultimateDrawableResId = R.drawable.find_icon,
                lastDrawableResId = R.drawable.filter_icon
            ),
            text = getString(R.string.news_app)
        )
        binding.headlinesAppBar.initializationView(appInitModel)
        binding.headlinesAppBar.setOnClickFun(
            penultimateFun = {
                NavigatorCompat.goTo(
                    FindFragment.newInstance(),
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
        val filterCount = filterViewModel.getFilterCount()
        binding.headlinesAppBar.setFilterBudge(filterCount)
    }

    private fun mapTabModel(headlinesTab: HeadlinesTab): TabModel {
        return when (headlinesTab) {
            HeadlinesTab.GENERAL -> TabModel(
                headlinesTab,
                R.drawable.general_tab_icon,
                requireContext().getString(R.string.headlines_tab_1),
                false
            )

            HeadlinesTab.BUSINESS -> TabModel(
                headlinesTab,
                R.drawable.business_tab_icon,
                requireContext().getString(R.string.headlines_tab_2),
                false
            )

            HeadlinesTab.TRAVELING -> TabModel(
                headlinesTab,
                R.drawable.traveling_tab_icon,
                requireContext().getString(R.string.headlines_tab_3),
                false
            )
        }
    }

    private fun setTabActive(
        headlinesTab: HeadlinesTab,
        tabList: MutableList<TabModel>,
        tabBarController: BarController
    ) {
        val index = tabList.indexOfFirst { it.headlinesTab == headlinesTab }
        tabList[index] = tabList[index].copy(isActive = true)
        tabBarController.changeFilterButton(
            { tabList[index] = tabList[index].copy(isActive = false) },
            { tabList[index] = tabList[index].copy(isActive = true) }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.coroutineContext.cancelChildren()
        _binding = null
    }
}


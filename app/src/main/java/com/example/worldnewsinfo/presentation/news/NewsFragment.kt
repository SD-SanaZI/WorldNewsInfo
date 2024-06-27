package com.example.worldnewsinfo.presentation.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.application.getSerializableCompat
import com.example.worldnewsinfo.databinding.FragmentRecyclerBinding
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.presentation.BaseFragment
import com.example.worldnewsinfo.presentation.appBar.AppBarDrawableRes
import com.example.worldnewsinfo.presentation.appBar.AppInitModel
import com.example.worldnewsinfo.presentation.appBar.Mode
import com.example.worldnewsinfo.presentation.di.MainComponent
import com.example.worldnewsinfo.presentation.model.NewsModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

const val NEWS_MODEL_KEY = "News_MODEL_KEY"

class NewsFragment : BaseFragment<FragmentRecyclerBinding>() {
    private lateinit var newsModel: NewsModel
    @Inject
    lateinit var repositoryImpl: Repository
    private val newsFragmentViewModel by lazy { ViewModelProvider(this, NewsFragmentViewModelFactory(newsModel, repositoryImpl))[NewsFragmentViewModel::class.java] }
    private var component: MainComponent? = null


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance(newsModel: NewsModel) : NewsFragment {
            val fragment = NewsFragment()
            val args = Bundle()
            args.putSerializable(NEWS_MODEL_KEY, newsModel)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (component == null)
            component = MainComponent.create(requireContext())
        requireNotNull(component).inject(this)
        if (arguments != null)
            newsModel = getSerializableCompat<NewsModel>(arguments, NEWS_MODEL_KEY) ?: throw Exception("Null newsModel")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        appBarSetting()

        val data = listOf(
            NewsPageModel(
                newsModel.shortNewsText,
                changeTimeString(newsModel.publishedAt),
                newsModel.sourceName,
                newsModel.text?.let { setMessageWithClickableLink(it,newsModel.newsUrl) },
                R.drawable.on_empty_news_text_icon
            )
        )
        val adapter = NewsPageAdapter()
        adapter.submitList(data)
        binding.mainRecycler.adapter = adapter
        return view
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                newsFragmentViewModel.stateFlow.collect{
                    binding.mainAppBar.findViewById<ImageView>(R.id.app_bar_button_last).setImageDrawable(
                        AppCompatResources.getDrawable(requireContext(),
                            if (it.isSaved) R.drawable.saved_on_icon
                            else R.drawable.saved_off_icon
                        )
                    )
                }
            }
        }
    }

    private fun changeTimeString(oldDateString: String):String{
        val dateFormatFrom = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
        val date = dateFormatFrom.parse(oldDateString)
        val dateFormatTo = SimpleDateFormat("MMM dd, yyyy | h:mm a", Locale.ROOT)
        return dateFormatTo.format(date!!)
    }

    private fun appBarSetting(){
        val appInitModel = AppInitModel(
            viewHeight = 160,
            backgroundImg = newsModel.newsImg,
            mode = Mode.FILTER,
            appBarDrawableRes = AppBarDrawableRes(
                firstDrawableResId = R.drawable.prev_icon,
                lastDrawableResId = R.drawable.saved_off_icon
            ),
            text = newsModel.shortNewsText
        )
        binding.mainAppBar.initializationView(appInitModel)
        binding.mainAppBar.setOnClickFun(
            firstFun = {
                NavigatorCompat.back()
            },
            lastFun = {
                newsFragmentViewModel.changeSavedState()
            }
        )
    }

    private fun setMessageWithClickableLink(text:String, url: String):  SpannableString {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                try{
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(requireContext(), intent, null);
                }catch (e:Exception){
                    Log.d("Exception", e.toString())
                }
            }
        }
        val startIndex = text.lastIndexOf("[")+1
        val endIndex = text.lastIndexOf("]")
        val spannableString = SpannableString(text)
        if(startIndex!= -1 && endIndex!= -1)
            spannableString.setSpan(clickableSpan, startIndex, endIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }
}


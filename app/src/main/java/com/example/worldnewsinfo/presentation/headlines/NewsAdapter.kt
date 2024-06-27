package com.example.worldnewsinfo.presentation.headlines

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.presentation.model.NewsModel
import com.example.worldnewsinfo.presentation.news.NewsFragment


class NewsAdapter(private val colorRes: Int? = null) :
    ListAdapter<NewsModel, NewsAdapter.ViewHolders>(NewsDiffUtil()) {
    private var isLoadingAdded = false

    sealed class ViewHolders(view: View) : RecyclerView.ViewHolder(view)
    class NewsViewHolder(view: View) : ViewHolders(view) {
        private val newsImg: ImageView
        private val sourceImg: ImageView
        private val sourceName: TextView
        private val shortNewsText: TextView

        init {
            newsImg = view.findViewById(R.id.news_item_news_img)
            sourceImg = view.findViewById(R.id.news_item_chanel_image)
            sourceName = view.findViewById(R.id.news_item_chanel_name)
            shortNewsText = view.findViewById(R.id.news_item_news_short_text)
        }

        fun bind(data: NewsModel, context: Context) {
            newsImg.load(data.newsImg) {
                crossfade(true)
                placeholder(R.drawable.placeholder_icon)
                error(R.drawable.painting_error_icon)
                transformations(RoundedCornersTransformation())
            }
            sourceImg.setImageDrawable(AppCompatResources.getDrawable(context, data.sourceImg))
            sourceName.text = data.sourceName
            shortNewsText.text = data.shortNewsText
        }
    }

    class LoadingViewHolder(view: View) : ViewHolders(view) {
        private val loading: ImageView

        init {
            loading = view.findViewById(R.id.progress_bar)
        }

        fun bind() {
            //TODO animate
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        return when (viewType) {
            ViewType.LOADING.num -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.progress_bar_item, parent, false)
                changeBackgroundView(view)
                LoadingViewHolder(view)
            }

            ViewType.NEWS.num -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_item, parent, false)
                changeBackgroundView(view)
                NewsViewHolder(view)
            }

            else -> throw Exception("Wrong ViewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val data = getItem(position)
        when (holder) {
            is LoadingViewHolder -> {
                holder.bind()
            }

            is NewsViewHolder -> {
                holder.bind(data, holder.itemView.context)
                holder.itemView.setOnClickListener {
                    NavigatorCompat.goTo(NewsFragment.newInstance(data), "")
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isLoadingAdded)
            ViewType.LOADING.num
        else
            ViewType.NEWS.num
    }

    fun startLoading() {
        isLoadingAdded = true
        val newList = currentList.toMutableList()
        newList.add(NewsModel(0, "", 0, "", "", "", "", "", ""))
        submitList(newList)
    }

    fun endLoading() {
        if(isLoadingAdded){
            isLoadingAdded = false
            val position: Int = currentList.size - 1
            val result = getItem(position)

            if (result != null) {
                val newList = currentList.toMutableList()
                newList.removeAt(position)
                submitList(newList)
                notifyItemRemoved(position)
            }
        }
    }

    fun getLoading(): Boolean {
        return isLoadingAdded
    }

    private fun changeBackgroundView(view: View) {
        if (colorRes != null) {
            val typedValue = TypedValue()
            view.context.theme.resolveAttribute(colorRes, typedValue, true)
            val colorInt = ContextCompat.getColor(view.context, typedValue.resourceId);
            view.background = ColorDrawable(colorInt)
        }
    }

    class NewsDiffUtil : DiffUtil.ItemCallback<NewsModel>() {
        override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem == newItem
        }
    }

    enum class ViewType(val num: Int) {
        LOADING(0), NEWS(1)
    }
}


package com.example.worldnewsinfo.presentation.news

import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.worldnewsinfo.R

class NewsPageAdapter :
    ListAdapter<NewsPageModel, NewsPageAdapter.NewsPageViewHolder>(NewsPageDiffUtil()) {
    class NewsPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val shortText: TextView
        private val supportingText: TextView
        private val source: TextView
        private val text: TextView
        private val image: ImageView

        init {
            shortText = view.findViewById(R.id.fragment_news_short_text)
            supportingText = view.findViewById(R.id.fragment_news_supporting_text)
            source = view.findViewById(R.id.fragment_news_source)
            text = view.findViewById(R.id.fragment_news_text)
            image = view.findViewById(R.id.fragment_news_img)
            text.movementMethod = LinkMovementMethod.getInstance()
            text.highlightColor = Color.TRANSPARENT
        }

        fun bind(data: NewsPageModel, context: Context) {
            shortText.text = data.shortText
            supportingText.text = data.supportingText
            source.text = data.source
            if (data.text != null) {
                text.visibility = View.VISIBLE
                image.visibility = View.GONE
                text.text = data.text
            } else {
                text.visibility = View.GONE
                image.visibility = View.VISIBLE
                image.setImageDrawable(AppCompatResources.getDrawable(context, data.onEmptyImg))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsPageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_page_item, parent, false)
        return NewsPageViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsPageViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView.context)
    }

    class NewsPageDiffUtil : DiffUtil.ItemCallback<NewsPageModel>() {
        override fun areItemsTheSame(oldItem: NewsPageModel, newItem: NewsPageModel): Boolean {
            return oldItem.shortText == newItem.shortText
        }

        override fun areContentsTheSame(oldItem: NewsPageModel, newItem: NewsPageModel): Boolean {
            return oldItem == newItem
        }

    }
}
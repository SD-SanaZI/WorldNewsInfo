package com.example.worldnewsinfo.presentation.source

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.presentation.sourseNews.SourceNewsFragment

class SourceAdapter: ListAdapter<SourceModel, SourceAdapter.SourceViewHolder>(TabDiffUtil()) {
    class SourceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sourceImg: ImageView
        private val sourceName: TextView
        private val supportingText: TextView

        init {
            sourceImg = view.findViewById(R.id.source_item_img)
            sourceName = view.findViewById(R.id.source_item_name)
            supportingText = view.findViewById(R.id.source_item_supporting_text)
        }

        fun bind(data: SourceModel) {
            sourceImg.load(data.sourceImg) {
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }
            sourceName.text = data.sourceName
            supportingText.text = data.supportingText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.source_item, parent, false)
        return SourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        holder.itemView.setOnClickListener {
            NavigatorCompat.goTo(
                SourceNewsFragment.newInstance(data.sourceId, data.sourceName),
                "SOURCES"
            )
        }
    }

    class TabDiffUtil : DiffUtil.ItemCallback<SourceModel>() {
        override fun areItemsTheSame(oldItem: SourceModel, newItem: SourceModel): Boolean {
            return oldItem.sourceId == newItem.sourceId
        }

        override fun areContentsTheSame(oldItem: SourceModel, newItem: SourceModel): Boolean {
            return oldItem == newItem
        }
    }
}


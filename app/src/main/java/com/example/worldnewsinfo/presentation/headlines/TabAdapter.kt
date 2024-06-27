package com.example.worldnewsinfo.presentation.headlines

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
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.presentation.model.TabModel

class TabAdapter(private val onClick: (position: Int) -> Unit) :
    ListAdapter<TabModel, TabAdapter.TabViewHolder>(TabDiffUtil()) {

    class TabViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tabItemImg: ImageView
        private val tabItemText: TextView
        private val tabItemIndicator: ImageView

        init {
            tabItemImg = view.findViewById(R.id.tab_item_img)
            tabItemText = view.findViewById(R.id.tab_item_text)
            tabItemIndicator = view.findViewById(R.id.tab_item_indicator)
        }

        fun bind(data: TabModel) {
            tabItemImg.load(data.img) {
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }
            tabItemText.text = data.text

            if (data.isActive)
                tabItemText.setTextAppearance(R.style.TapBarTextActive)
            else
                tabItemText.setTextAppearance(R.style.TapBarText)
            tabItemIndicator.visibility = if (data.isActive) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tab_item, parent, false)
        view.layoutParams.width = parent.width / 3
        return TabViewHolder(view)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    class TabDiffUtil : DiffUtil.ItemCallback<TabModel>() {
        override fun areItemsTheSame(oldItem: TabModel, newItem: TabModel): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: TabModel, newItem: TabModel): Boolean {
            return oldItem == newItem
        }
    }
}


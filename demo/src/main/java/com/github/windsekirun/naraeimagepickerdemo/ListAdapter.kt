package com.github.windsekirun.naraeimagepickerdemo

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.windsekirun.naraeimagepicker.utils.loadImage

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListHolder>() {
    private val itemList = mutableListOf<String>()

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ListHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false))

    fun setItems(items: List<String>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)

        fun bind(item: String) {
            imgThumbnail.loadImage(item, 1.0f)
        }
    }
}
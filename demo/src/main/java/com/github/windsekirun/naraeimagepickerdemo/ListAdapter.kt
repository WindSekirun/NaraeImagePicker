package com.github.windsekirun.naraeimagepickerdemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.naraeimagepicker.utils.loadImage
import kotlinx.android.synthetic.main.main_item.view.*

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

        fun bind(item: String) {
            itemView.imgThumbnail.loadImage(item, 1.0f)
        }
    }
}
package com.github.windsekirun.naraeimagepicker.fragment.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.naraeimagepicker.event.DetailEvent
import com.github.windsekirun.naraeimagepicker.item.FileItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import com.github.windsekirun.naraeimagepicker.utils.loadImage
import kotlinx.android.synthetic.main.fragment_image_row.view.*
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R

/**
 * NaraeImagePicker
 * Class: ImageAdapter
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageAdapter(private val itemList: ArrayList<FileItem>,
                   val listener: (FileItem, Int, View) -> Unit) :
        RecyclerView.Adapter<ImageAdapter.ListHolder>() {

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ListHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_image_row, parent, false))

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: FileItem) {
            itemView.imgThumbnail.loadImage(item.imagePath, 0.3f)
            itemView.imgCheck.isSelected = SelectedItem.contains(item)
            itemView.opacity.isSelected = SelectedItem.contains(item)
            itemView.btnMaximise.visibility = if (PickerSet.getSettingItem().enableDetailMode) View.VISIBLE else View.GONE

            itemView.btnMaximise.setOnClickListener { EventBus.getDefault().post(DetailEvent(item.imagePath)) }
            itemView.setOnClickListener {
                listener(item, adapterPosition, it)
            }
        }
    }
}

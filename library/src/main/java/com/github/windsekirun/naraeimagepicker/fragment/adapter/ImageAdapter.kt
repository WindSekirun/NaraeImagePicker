package com.github.windsekirun.naraeimagepicker.fragment.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.windsekirun.naraeimagepicker.event.DetailEvent
import com.github.windsekirun.naraeimagepicker.item.FileItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import com.github.windsekirun.naraeimagepicker.utils.loadImage
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
        private val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
        private val imgCheck: ImageView = itemView.findViewById(R.id.imgCheck)
        private val opacity: View = itemView.findViewById(R.id.opacity)
        private val btnMaximise: ImageView = itemView.findViewById(R.id.btnMaximise)

        fun bind(item: FileItem) {
            imgThumbnail.loadImage(item.imagePath, 0.3f)
            imgCheck.isSelected = SelectedItem.contains(item)
            opacity.isSelected = SelectedItem.contains(item)
            btnMaximise.visibility = if (PickerSet.getSettingItem().enableDetailMode) View.VISIBLE else View.GONE

            btnMaximise.setOnClickListener { EventBus.getDefault().post(DetailEvent(item.imagePath)) }
            itemView.setOnClickListener {
                listener(item, adapterPosition, it)
            }
        }
    }
}

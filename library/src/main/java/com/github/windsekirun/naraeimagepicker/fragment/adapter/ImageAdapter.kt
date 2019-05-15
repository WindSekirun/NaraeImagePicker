package com.github.windsekirun.naraeimagepicker.fragment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_image_row.view.*
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.utils.loadImage
import pyxis.uzuki.live.richutilskt.utils.toast

/**
 * NaraeImagePicker
 * Class: ImageAdapter
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageAdapter(val mContext: Context, val itemList: ArrayList<com.github.windsekirun.naraeimagepicker.item.ImageItem>,
                   val listener: (com.github.windsekirun.naraeimagepicker.item.ImageItem, Int, View) -> Unit) :
        RecyclerView.Adapter<com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter.ListHolder>() {

    override fun onBindViewHolder(holder: com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter.ListHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ListHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_image_row, parent, false))

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: com.github.windsekirun.naraeimagepicker.item.ImageItem) {
            itemView.imgThumbnail.loadImage(item.imagePath, 0.3f)
            itemView.imgCheck.isSelected = com.github.windsekirun.naraeimagepicker.module.SelectedItem.contains(item)
            itemView.opacity.isSelected = com.github.windsekirun.naraeimagepicker.module.SelectedItem.contains(item)
            itemView.btnMaximise.visibility = if (!com.github.windsekirun.naraeimagepicker.module.PickerSet.getSettingItem().disableZoomMode) View.VISIBLE else View.GONE

            itemView.btnMaximise.setOnClickListener { EventBus.getDefault().post(com.github.windsekirun.naraeimagepicker.event.DetailEvent(item.imagePath)) }
            itemView.setOnClickListener {
                /*if (SelectedItem.contains(item)) {
                    SelectedItem.removeItem(item)
                } else {
                    addSelectedItem(item)
                }
                notifyDataSetChanged()*/
                listener(item, adapterPosition, it)
            }
        }

        private fun addSelectedItem(item: com.github.windsekirun.naraeimagepicker.item.ImageItem) {
            com.github.windsekirun.naraeimagepicker.module.SelectedItem.addItem(item) {
                if (!it) mContext.toast(com.github.windsekirun.naraeimagepicker.module.PickerSet.getLimitMessage())
            }
        }
    }
}

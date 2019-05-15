package com.github.windsekirun.naraeimagepicker.fragment.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.naraeimagepicker.event.FragmentTransitionEvent
import com.github.windsekirun.naraeimagepicker.item.AlbumItem
import com.github.windsekirun.naraeimagepicker.utils.loadImage
import kotlinx.android.synthetic.main.fragment_album_row.view.*
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R

/**
 * NaraeImagePicker
 * Class: AlbumAdapter
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AlbumAdapter(private val itemList: ArrayList<AlbumItem>) : RecyclerView.Adapter<AlbumAdapter.ListHolder>() {

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ListHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_album_row, parent, false))

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: AlbumItem) {
            itemView.txtName.text = item.name
            itemView.imgThumbnail.loadImage(item.imagePath, 0.3f)
            itemView.setOnClickListener { EventBus.getDefault().post(FragmentTransitionEvent(true, item.name)) }
        }
    }
}
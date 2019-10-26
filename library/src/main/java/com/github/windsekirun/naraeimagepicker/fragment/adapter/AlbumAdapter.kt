package com.github.windsekirun.naraeimagepicker.fragment.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.windsekirun.naraeimagepicker.event.FragmentTransitionEvent
import com.github.windsekirun.naraeimagepicker.item.FolderItem
import com.github.windsekirun.naraeimagepicker.utils.loadImage
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R

/**
 * NaraeImagePicker
 * Class: AlbumAdapter
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AlbumAdapter(private val itemList: ArrayList<FolderItem>) : RecyclerView.Adapter<AlbumAdapter.ListHolder>() {

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ListHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_album_row, parent, false))

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.txtName)
        private val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)

        @SuppressLint("SetTextI18n")
        fun bind(item: FolderItem) {
            txtName.text = item.name
            imgThumbnail.loadImage(item.imagePath, 0.3f)
            itemView.setOnClickListener { EventBus.getDefault().post(FragmentTransitionEvent(true, item.name)) }
        }
    }
}
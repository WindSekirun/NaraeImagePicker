package pyxis.uzuki.live.naraeimagepicker.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_album_row.view.*
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.FragmentTransitionEvent
import pyxis.uzuki.live.naraeimagepicker.item.AlbumItem
import pyxis.uzuki.live.naraeimagepicker.module.GlideApp

/**
 * NaraeImagePicker
 * Class: AlbumAdapter
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AlbumAdapter(val mContext: Context, val itemList: ArrayList<AlbumItem>) :
        RecyclerView.Adapter<AlbumAdapter.ListHolder>() {

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder?.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ListHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_album_row, parent, false))

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: AlbumItem) {
            itemView.txtName.text = item.name
            GlideApp.with(mContext).load(item.imagePath).thumbnail(0.3f).into(itemView.imgThumbnail)
            itemView.setOnClickListener { EventBus.getDefault().post(FragmentTransitionEvent(true, item.name)) }
        }
    }
}
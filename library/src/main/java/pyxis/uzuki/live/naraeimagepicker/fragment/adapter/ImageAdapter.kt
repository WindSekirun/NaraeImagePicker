package pyxis.uzuki.live.naraeimagepicker.fragment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_image_row.view.*
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.DetailEvent
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.module.PickerSet
import pyxis.uzuki.live.naraeimagepicker.module.SelectedItem
import pyxis.uzuki.live.naraeimagepicker.utils.loadImage
import pyxis.uzuki.live.richutilskt.utils.toast

/**
 * NaraeImagePicker
 * Class: ImageAdapter
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageAdapter(val mContext: Context, val itemList: ArrayList<ImageItem>) :
        RecyclerView.Adapter<ImageAdapter.ListHolder>() {

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ListHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_image_row, parent, false))

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ImageItem) {
            itemView.imgThumbnail.loadImage(item.imagePath, 0.3f)
            itemView.imgCheck.isSelected = SelectedItem.contains(item)
            itemView.opacity.isSelected = SelectedItem.contains(item)
            itemView.btnMaximise.visibility = if (!PickerSet.getSettingItem().disableZoomMode) View.VISIBLE else View.GONE

            itemView.btnMaximise.setOnClickListener { EventBus.getDefault().post(DetailEvent(item.imagePath)) }
            itemView.setOnClickListener {
                if (SelectedItem.contains(item)) {
                    SelectedItem.removeItem(item)
                } else {
                    addSelectedItem(item)
                }
                notifyDataSetChanged()
            }
        }

        private fun addSelectedItem(item: ImageItem) {
            SelectedItem.addItem(item) {
                if (!it) mContext.toast(PickerSet.getLimitMessage())
            }
        }
    }
}

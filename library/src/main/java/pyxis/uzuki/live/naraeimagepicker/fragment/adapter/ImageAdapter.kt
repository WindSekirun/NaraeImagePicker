package pyxis.uzuki.live.naraeimagepicker.fragment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_image_row.view.*
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.DetailEvent
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.module.SelectedItem

/**
 * NaraeImagePicker
 * Class: ImageAdapter
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageAdapter(val mContext: Context, val itemList: ArrayList<ImageItem>) :
        RecyclerView.Adapter<ImageAdapter.ListHolder>() {

    override fun onBindViewHolder(holder: ListHolder?, position: Int) {
        holder?.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            ListHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_image_row, parent, false))

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ImageItem) {
            Glide.with(mContext).load(item.imagePath).thumbnail(0.5f).into(itemView.imgThumbnail)
            itemView.imgCheck.isSelected = SelectedItem.contains(item)
            itemView.opacity.isSelected = SelectedItem.contains(item)

            itemView.btnMaximise.setOnClickListener {
                EventBus.getDefault().post(DetailEvent(item.imagePath))
            }

            itemView.setOnClickListener {
                if (SelectedItem.contains(item)) {
                    SelectedItem.removeItem(item)
                    notifyDataSetChanged()
                    return@setOnClickListener
                }

                SelectedItem.addItem(item, {
                    if (!it) {
                        Toast.makeText(mContext,
                                mContext.getString(R.string.narae_image_picker_limit_exceed).format(SelectedItem.getLimits()),
                                Toast.LENGTH_SHORT).show()
                    }

                    notifyDataSetChanged()
                })
            }
        }
    }
}
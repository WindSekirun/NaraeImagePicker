package pyxis.uzuki.live.naraeimagepicker.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_album_row.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.base.BaseFragment
import pyxis.uzuki.live.naraeimagepicker.event.FragmentTransitionEvent
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.item.AlbumItem
import pyxis.uzuki.live.naraeimagepicker.utils.AdjustableGridItemDecoration
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import pyxis.uzuki.live.richutilskt.utils.toFile

/**
 * NaraeImagePicker
 * Class: AlbumFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AlbumFragment : BaseFragment() {
    private val adapter = ListAdapter()
    private val itemList = arrayListOf<AlbumItem>()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendEvent(ToolbarEvent(getString(R.string.narae_image_picker_album_title)))

        val rectF = AdjustableGridItemDecoration.getRectFObject(activity)

        recyclerView.mEmptyView = containerEmpty
        recyclerView.mLoadingView = progressBar
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(AdjustableGridItemDecoration(rectF, itemList, 3))

        runAsync { loadItem() }
    }

    private fun loadItem() {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"
        val cursor = activity.contentResolver.query(uri, projection, null, null, orderBy)
        val displayNameColumn = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        val pathColumn = MediaStore.Images.Media.DATA
        val items = HashSet<AlbumItem>()

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val album = cursor.getColumnString(displayNameColumn)
                    val image = cursor.getColumnString(pathColumn)
                    val file = image.toFile()

                    if (!file.exists()) continue

                    val fileList = file.parentFile.list() ?: continue
                    items.add(AlbumItem(album, image, fileList.size))
                } while (cursor.moveToNext())
            }
        }

        itemList.addAll(items)
        items.clear()
        runOnUiThread { recyclerView.notifyDataSetChanged() }
    }

    inner class ListAdapter : RecyclerView.Adapter<ListHolder>() {
        override fun onBindViewHolder(holder: ListHolder?, position: Int) {
            holder?.bind(itemList[position])
        }

        override fun getItemCount() = itemList.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                ListHolder(LayoutInflater.from(activity).inflate(R.layout.fragment_album_row, parent, false))
    }


    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: AlbumItem) {
            itemView.txtName.text = "${item.name} (${item.itemCount})"
            Glide.with(activity).load(item.imagePath).thumbnail(0.5f).into(itemView.imgThumbnail)
            itemView.setOnClickListener { sendEvent(FragmentTransitionEvent(true, item.name)) }
        }
    }
}
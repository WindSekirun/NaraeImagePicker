package pyxis.uzuki.live.naraeimagepicker.folder

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
import kotlinx.android.synthetic.main.fragment_main.*
import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.base.BaseFragment
import pyxis.uzuki.live.naraeimagepicker.event.FragmentTransitionEvent
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.item.AlbumItem
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import pyxis.uzuki.live.richutilskt.utils.toFile
import java.nio.file.Files.exists



/**
 * NaraeImagePicker
 * Class: ImageFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageFragment : BaseFragment() {
    private val adapter = ListAdapter()
    private val itemList = arrayListOf<ImageItem>()

    private var albumName = ""
    private var imageCount = ""

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumName = arguments.getString(Constants.EXTRA_NAME)
        imageCount = arguments.getString(Constants.EXTRA_ITEM_COUNT)

        sendEvent(ToolbarEvent("$albumName ($imageCount)", true))

        recyclerView.mEmptyView = containerEmpty
        recyclerView.mLoadingView = progressBar
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.setHasFixedSize(true)

        runAsync { loadItem() }
    }

    private fun loadItem() {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?"
        val selectionArg = arrayOf(albumName)
        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"
        val cursor = activity.contentResolver.query(uri, projection, selection, selectionArg, orderBy)
        val idColumn = MediaStore.Images.Media._ID
        val pathColumn = MediaStore.Images.Media.DATA
        val items = HashSet<ImageItem>()

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val image = cursor.getColumnString(pathColumn)
                    val id = cursor.getColumnString(idColumn)
                    val file = image.toFile()

                    if (!file.exists()) continue

                    items.add(ImageItem(id, image))
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
                ListHolder(LayoutInflater.from(activity).inflate(R.layout.fragment_image_row, parent, false))
    }


    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ImageItem) {
            Glide.with(activity).load(item.imagePath).thumbnail(0.5f).into(itemView.imgThumbnail)
            itemView.setOnClickListener {

            }
        }
    }
}
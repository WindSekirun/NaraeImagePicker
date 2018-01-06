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
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.utils.clearAndTrim
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.toFile
import java.io.Serializable

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

        recyclerView.mEmptyView = containerEmpty
        recyclerView.mLoadingView = progressBar
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.setHasFixedSize(true)

        runAsync {
            loadItem()
        }
    }

    private fun loadItem() {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val orderBy = MediaStore.Images.Media.DATE_ADDED
        val cursor = activity.contentResolver.query(uri, projection, null, null, orderBy)

        val displayNameColumn = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        val pathColumn = MediaStore.Images.Media.DATA

        val items = arrayListOf<AlbumItem>()
        val names = arrayListOf<String>()

        if (cursor.moveToLast()) {
            do {
                val album = cursor.getColumnString(displayNameColumn)
                val image = cursor.getColumnString(pathColumn)
                val file = image.toFile()

                if (!file.exists()) {
                    continue
                }

                val fileList = file.parentFile.list() ?: continue

                if (names.contains(album)) {
                    continue
                }

                items.add(AlbumItem(album, image, fileList.size))
                names.add(album)
            } while (cursor.moveToPrevious())
        }

        cursor.close()

        itemList.addAll(items)
        items.clearAndTrim()
        names.clearAndTrim()
        recyclerView.notifyDataSetChanged()
    }

    inner class ListAdapter : RecyclerView.Adapter<ListHolder>() {
        override fun onBindViewHolder(holder: ListHolder?, position: Int) {
            holder?.bind(itemList[position])
        }

        override fun getItemCount() = itemList.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                ListHolder(LayoutInflater.from(activity).inflate(R.layout.fragment_album_row, parent, false))
    }

    data class AlbumItem(val name: String, val imagePath: String, val itemCount: Int) : Serializable

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: AlbumItem) {
            itemView.txtName.text = "${item.name} (${item.itemCount})"
            Glide.with(activity).load(item.imagePath).thumbnail(0.5f).into(itemView.imgThumbnail)
        }
    }
}
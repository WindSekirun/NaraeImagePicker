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
import pyxis.uzuki.live.naraeimagepicker.fragment.adapter.AlbumAdapter
import pyxis.uzuki.live.naraeimagepicker.item.AlbumItem
import pyxis.uzuki.live.naraeimagepicker.widget.AdjustableGridItemDecoration
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

class AlbumFragment : BaseFragment<AlbumItem>() {
    private lateinit var adapter: AlbumAdapter
    private val itemList = arrayListOf<AlbumItem>()

    override fun getItemList() = itemList

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AlbumAdapter(activity, itemList)
        recyclerView.adapter = adapter
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
}
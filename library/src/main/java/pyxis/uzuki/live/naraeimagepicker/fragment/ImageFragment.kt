package pyxis.uzuki.live.naraeimagepicker.fragment

import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.base.BaseFragment
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.fragment.adapter.ImageAdapter
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import pyxis.uzuki.live.richutilskt.utils.toFile

/**
 * NaraeImagePicker
 * Class: ImageFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageFragment : BaseFragment<ImageItem>() {
    private lateinit var adapter: ImageAdapter
    private val itemList = arrayListOf<ImageItem>()

    private var albumName = ""

    override fun getItemList() = itemList

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumName = arguments.getString(Constants.EXTRA_NAME)
        sendEvent(ToolbarEvent(albumName, true))

        adapter = ImageAdapter(activity, itemList)
        recyclerView.adapter = adapter
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

        runOnUiThread {
            recyclerView.notifyDataSetChanged()
            sendEvent(ToolbarEvent("$albumName (${itemList.size})", true))
        }
    }
}
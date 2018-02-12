package pyxis.uzuki.live.naraeimagepicker.fragment

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.naraeimagepicker.base.BaseFragment
import pyxis.uzuki.live.naraeimagepicker.fragment.adapter.AlbumAdapter
import pyxis.uzuki.live.naraeimagepicker.item.AlbumItem
import pyxis.uzuki.live.naraeimagepicker.utils.TimeLogger
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
    private lateinit var timeLogger: TimeLogger

    override fun getItemList() = itemList
    override fun getItemKind() = AlbumItem::class.simpleName ?: ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timeLogger = TimeLogger(AlbumFragment::class.java.simpleName, "loadItem")
        timeLogger.addPart("start")
        adapter = AlbumAdapter(context as Context, itemList)
        recyclerView.adapter = adapter

        runAsync { loadItem() }
    }

    private fun loadItem() {
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val cursor = (context as Context).contentResolver.query(cursorUri, projection, null, null, orderBy)
        val items = HashSet<AlbumItem>()

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                val album = cursor.getColumnString(displayNameColumn)
                val image = cursor.getColumnString(pathColumn)
                val file = image.toFile()

                if (!file.exists()) continue

                items.add(AlbumItem(album, image))
            }
        }

        cursor.close()
        itemList.addAll(items)
        items.clear()
        runOnUiThread {
            if (recyclerView != null) {
                recyclerView.notifyDataSetChanged()
            }
        }
    }
}
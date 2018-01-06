package pyxis.uzuki.live.naraeimagepicker.fragment

import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.naraeimagepicker.base.BaseFragment
import pyxis.uzuki.live.naraeimagepicker.fragment.adapter.ImageAdapter
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import pyxis.uzuki.live.richutilskt.utils.toFile


/**
 * NaraeImagePicker
 * Class: AllFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AllFragment : BaseFragment<ImageItem>() {
    private lateinit var adapter: ImageAdapter
    private val itemList = arrayListOf<ImageItem>()

    override fun getItemList() = itemList
    override fun getItemKind() = ImageItem::class.simpleName ?: ""

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ImageAdapter(activity, itemList)
        recyclerView.adapter = adapter
        runAsync { loadItem() }
    }

    private fun loadItem() {
        var projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val cursor = activity.contentResolver.query(cursorUri, projection, null, null, orderBy)
        val albumNameList = ArrayList<String>()
        val items = HashSet<ImageItem>()

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val album = cursor.getColumnString(displayNameColumn)
                    if (!albumNameList.contains(album)) {
                        albumNameList.add(album)
                    }
                } while (cursor.moveToNext())
            }
        }

        projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?"

        if (albumNameList.isEmpty()) {
            runOnUiThread { recyclerView.notifyDataSetChanged() }
        }

        albumNameList
                .map { arrayOf(it) }
                .map { activity.contentResolver.query(cursorUri, projection, selection, it, orderBy) }
                .forEach {
                    it.use {
                        if (it.moveToFirst()) {
                            do {
                                val image = it.getColumnString(pathColumn)
                                val id = it.getColumnString(idColumn)
                                val file = image.toFile()

                                if (!file.exists()) continue

                                items.add(ImageItem(id, image))
                            } while (it.moveToNext())
                        }
                    }
                }

        val tempItems = arrayListOf<ImageItem>()
        tempItems.addAll(items)

        items.sortedBy { it.imagePath.toFile().lastModified() }

        itemList.addAll(items)
        items.clear()

        runOnUiThread { recyclerView.notifyDataSetChanged() }
    }
}
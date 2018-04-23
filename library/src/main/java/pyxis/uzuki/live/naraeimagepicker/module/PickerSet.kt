package pyxis.uzuki.live.naraeimagepicker.module

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import pyxis.uzuki.live.naraeimagepicker.item.AlbumItem
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.item.PickerSettingItem
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.richutilskt.utils.toFile

/**
 * NaraeImagePicker
 * Class: PickerSet
 * Created by Pyxis on 2018-02-27.
 *
 * Description:
 */
object PickerSet {
    private lateinit var mItem: PickerSettingItem
    private val mPictureMap: HashMap<String, MutableList<ImageItem>> = hashMapOf()

    private val cursorUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private const val ID_COLUMN = MediaStore.Images.Media._ID
    private const val PATH_COLUMN = MediaStore.Images.Media.DATA
    private const val DISPLAY_NAME_COLUMN = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    private const val ORDER_BY = MediaStore.Images.Media.DATE_TAKEN

    fun clearPickerSet() {
        mItem.clear()
        mPictureMap.clear()
    }

    fun setSettingItem(item: PickerSettingItem) {
        mItem = item
    }

    fun getSettingItem() = mItem

    fun loadImageFirst(context: Context, callback: () -> Unit) {
        val titleProjection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, PATH_COLUMN)
        val titleCursor = context.contentResolver.query(cursorUri, titleProjection, null, null, ORDER_BY)
        val titleItemSet = HashSet<String>()

        titleCursor.use {
            if (titleCursor.moveToFirst()) {
                do {
                    val album = titleCursor.getColumnString(DISPLAY_NAME_COLUMN)
                    titleItemSet.add(album)
                } while (titleCursor.moveToNext())
            }
        }

        val titleItemList = mutableListOf<String>()
        titleItemList.addAll(titleItemSet)
        titleItemList.sort()

        for (title in titleItemList) {
            val photoPr = arrayOf(ID_COLUMN, PATH_COLUMN, MediaStore.Images.Media.DATE_MODIFIED)
            val photoSelection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?"
            val photoAlbumName = arrayOf(title)
            val photoCursor = context.contentResolver.query(cursorUri, photoPr, photoSelection, photoAlbumName, ORDER_BY)

            val list = mutableListOf<ImageItem>()
            photoCursor.use {
                if (photoCursor.moveToFirst()) {
                    do {
                        val image = photoCursor.getColumnString(PATH_COLUMN)
                        val id = photoCursor.getColumnString(ID_COLUMN)
                        val file = image.toFile()

                        if (!file.exists()) continue

                        list.add(ImageItem(id, image))
                    } while (photoCursor.moveToNext())
                }
            }

            list.reverse()
            addPictureMap(title, list)
        }

        titleItemSet.clear()
        titleItemList.clear()
        callback.invoke()
    }

    private fun addPictureMap(title: String, list: MutableList<ImageItem>) {
        mPictureMap[title] = list
    }

    fun getFolderList(): List<AlbumItem> {
        return try {
            mPictureMap.entries.map { it.key to it.value[0] }.map { AlbumItem(it.first, it.second.imagePath) }.sortedBy { it.name.toLowerCase() }.toList()
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    fun getImageList(title: String): MutableList<ImageItem> {
        return mPictureMap[title]?.toMutableList() ?: mutableListOf()
    }

    fun getImageList(): MutableList<ImageItem> {
        val list = mutableListOf<ImageItem>()
        mPictureMap.values.forEach { list.addAll(it) }
        return list
    }

    fun isEmptyList() = mPictureMap.isEmpty()
}
package pyxis.uzuki.live.naraeimagepicker.module

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
import android.provider.MediaStore.Images.Media.DATE_MODIFIED
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
        val titleCursor = context.contentResolver.query(cursorUri, arrayOf(BUCKET_DISPLAY_NAME, PATH_COLUMN), null, null, ORDER_BY)

        val titleItemSet = HashSet<String>()
        titleCursor.doWhile { titleItemSet.add(titleCursor.getColumnString(DISPLAY_NAME_COLUMN)) }

        val titleItemList = mutableListOf<String>().apply {
            addAll(titleItemSet)
            sort()
        }

        for (title in titleItemList) {
            val list = mutableListOf<ImageItem>()
            val photoCursor = context.contentResolver.query(cursorUri,
                    arrayOf(ID_COLUMN, PATH_COLUMN, DATE_MODIFIED), "$BUCKET_DISPLAY_NAME =?", arrayOf(title), ORDER_BY)

            photoCursor.doWhile {
                val image = photoCursor.getColumnString(PATH_COLUMN)
                val id = photoCursor.getColumnString(ID_COLUMN)
                val file = image.toFile()
                if (file.exists()) list.add(ImageItem(id, image))
            }

            list.reverse()
            addPictureMap(title, list)
        }

        titleItemSet.clear()
        titleItemList.clear()
        callback.invoke()
    }

    fun getFolderList(): List<AlbumItem> {
        return mPictureMap.entries
                .filter { it.value.isNotEmpty() }
                .map { it.key to it.value[0] }
                .map { AlbumItem(it.first, it.second.imagePath) }
                .sortedBy { it.name.toLowerCase() }
                .toList()
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

    fun getLimitMessage() = mItem.uiSetting.exceedLimitMessage.format(SelectedItem.getLimits())

    private fun Cursor.doWhile(action: () -> Unit) {
        this.use {
            if (this.moveToFirst()) {
                do {
                    action()
                } while (this.moveToNext())
            }
        }
    }

    private fun addPictureMap(title: String, list: MutableList<ImageItem>) {
        mPictureMap[title] = list
    }
}
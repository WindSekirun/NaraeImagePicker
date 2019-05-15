package com.github.windsekirun.naraeimagepicker.module

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
import android.provider.MediaStore.Images.Media.DATE_MODIFIED
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString

/**
 * NaraeImagePicker
 * Class: PickerSet
 * Created by Pyxis on 2018-02-27.
 *
 * Description:
 */
object PickerSet {
    private lateinit var mItem: com.github.windsekirun.naraeimagepicker.item.PickerSettingItem
    private val mPictureMap: HashMap<String, MutableList<com.github.windsekirun.naraeimagepicker.item.ImageItem>> = hashMapOf()

    private val cursorUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private const val ID_COLUMN = MediaStore.Images.Media._ID
    private const val PATH_COLUMN = MediaStore.Images.Media.DATA
    private const val DISPLAY_NAME_COLUMN = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    private const val ORDER_BY = MediaStore.Images.Media.DATE_TAKEN

    fun clearPickerSet() {
        com.github.windsekirun.naraeimagepicker.module.PickerSet.mItem.clear()
        com.github.windsekirun.naraeimagepicker.module.PickerSet.mPictureMap.clear()
    }

    fun setSettingItem(item: com.github.windsekirun.naraeimagepicker.item.PickerSettingItem) {
        com.github.windsekirun.naraeimagepicker.module.PickerSet.mItem = item
    }

    fun getSettingItem() = com.github.windsekirun.naraeimagepicker.module.PickerSet.mItem

    fun loadImageFirst(context: Context, callback: () -> Unit) {
        val titleCursor = context.contentResolver.query(com.github.windsekirun.naraeimagepicker.module.PickerSet.cursorUri, arrayOf(BUCKET_DISPLAY_NAME, com.github.windsekirun.naraeimagepicker.module.PickerSet.PATH_COLUMN), null, null, com.github.windsekirun.naraeimagepicker.module.PickerSet.ORDER_BY)

        val titleItemSet = HashSet<String>()
        titleCursor.doWhile { titleItemSet.add(titleCursor.getColumnString(com.github.windsekirun.naraeimagepicker.module.PickerSet.DISPLAY_NAME_COLUMN)) }

        val titleItemList = mutableListOf<String>().apply {
            addAll(titleItemSet)
            sort()
        }

        for (title in titleItemList) {
            val list = mutableListOf<com.github.windsekirun.naraeimagepicker.item.ImageItem>()
            val photoCursor = context.contentResolver.query(com.github.windsekirun.naraeimagepicker.module.PickerSet.cursorUri,
                    arrayOf(com.github.windsekirun.naraeimagepicker.module.PickerSet.ID_COLUMN, com.github.windsekirun.naraeimagepicker.module.PickerSet.PATH_COLUMN, DATE_MODIFIED), "$BUCKET_DISPLAY_NAME =?", arrayOf(title), com.github.windsekirun.naraeimagepicker.module.PickerSet.ORDER_BY)

            photoCursor.doWhile {
                val image = photoCursor.getColumnString(com.github.windsekirun.naraeimagepicker.module.PickerSet.PATH_COLUMN)
                val id = photoCursor.getColumnString(com.github.windsekirun.naraeimagepicker.module.PickerSet.ID_COLUMN)
                val file = image.toFile()
                if (file.exists()) list.add(com.github.windsekirun.naraeimagepicker.item.ImageItem(id, image))
            }

            list.reverse()
            com.github.windsekirun.naraeimagepicker.module.PickerSet.addPictureMap(title, list)
        }

        titleItemSet.clear()
        titleItemList.clear()
        callback.invoke()
    }

    fun getFolderList(): List<com.github.windsekirun.naraeimagepicker.item.AlbumItem> {
        return com.github.windsekirun.naraeimagepicker.module.PickerSet.mPictureMap.entries
                .filter { it.value.isNotEmpty() }
                .map { it.key to it.value[0] }
                .map { com.github.windsekirun.naraeimagepicker.item.AlbumItem(it.first, it.second.imagePath) }
                .sortedBy { it.name.toLowerCase() }
                .toList()
    }

    fun getImageList(title: String): MutableList<com.github.windsekirun.naraeimagepicker.item.ImageItem> {
        return com.github.windsekirun.naraeimagepicker.module.PickerSet.mPictureMap[title]?.toMutableList() ?: mutableListOf()
    }

    fun getImageList(): MutableList<com.github.windsekirun.naraeimagepicker.item.ImageItem> {
        val list = mutableListOf<com.github.windsekirun.naraeimagepicker.item.ImageItem>()
        com.github.windsekirun.naraeimagepicker.module.PickerSet.mPictureMap.values.forEach { list.addAll(it) }
        return list
    }

    fun isEmptyList() = com.github.windsekirun.naraeimagepicker.module.PickerSet.mPictureMap.isEmpty()

    fun getLimitMessage() = com.github.windsekirun.naraeimagepicker.module.PickerSet.mItem.uiSetting.exceedLimitMessage.format(com.github.windsekirun.naraeimagepicker.module.SelectedItem.getLimits())

    private fun Cursor.doWhile(action: () -> Unit) {
        this.use {
            if (this.moveToFirst()) {
                do {
                    action()
                } while (this.moveToNext())
            }
        }
    }

    private fun addPictureMap(title: String, list: MutableList<com.github.windsekirun.naraeimagepicker.item.ImageItem>) {
        com.github.windsekirun.naraeimagepicker.module.PickerSet.mPictureMap[title] = list
    }
}
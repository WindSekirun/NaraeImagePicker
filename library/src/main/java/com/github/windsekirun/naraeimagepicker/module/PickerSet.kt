package com.github.windsekirun.naraeimagepicker.module

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
import android.provider.MediaStore.Images.Media.DATE_MODIFIED
import com.github.windsekirun.naraeimagepicker.item.FileItem
import com.github.windsekirun.naraeimagepicker.item.FolderItem
import com.github.windsekirun.naraeimagepicker.item.PickerSettingItem
import com.github.windsekirun.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.richutilskt.utils.toFile
import java.util.*
import kotlin.collections.HashSet

/**
 * NaraeImagePicker
 * Class: PickerSet
 * Created by Pyxis on 2018-02-27.
 *
 * Description:
 */
object PickerSet {
    private lateinit var item: PickerSettingItem
    private val PICTURE_MAP: HashMap<String, MutableList<FileItem>> = hashMapOf()

    private val cursorUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private const val ID_COLUMN = MediaStore.Images.Media._ID
    private const val PATH_COLUMN = MediaStore.Images.Media.DATA
    private const val DISPLAY_NAME_COLUMN = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    private const val ORDER_BY = MediaStore.Images.Media.DATE_TAKEN

    fun clearPickerSet() {
        item.clear()
        PICTURE_MAP.clear()
    }

    fun setSettingItem(item: PickerSettingItem) {
        this.item = item
    }

    fun getSettingItem() = item

    fun loadImageFirst(context: Context, callback: () -> Unit) {
        val titleCursor = context.contentResolver.query(cursorUri, arrayOf(BUCKET_DISPLAY_NAME, PATH_COLUMN), null, null, ORDER_BY)

        val titleItemSet = HashSet<String>()
        titleCursor?.doWhile { titleItemSet.add(titleCursor.getColumnString(DISPLAY_NAME_COLUMN)) }

        val titleItemList = mutableListOf<String>().apply {
            addAll(titleItemSet)
            sort()
        }

        for (title in titleItemList) {
            val list = mutableListOf<FileItem>()
            val photoCursor = context.contentResolver.query(cursorUri,
                    arrayOf(ID_COLUMN, PATH_COLUMN, DATE_MODIFIED), "$BUCKET_DISPLAY_NAME =?", arrayOf(title), ORDER_BY)

            photoCursor?.doWhile {
                val image = photoCursor.getColumnString(PATH_COLUMN)
                val id = photoCursor.getColumnString(ID_COLUMN)
                val file = image.toFile()
                if (file.exists())
                    if (item.includeGif) {
                        list.add(FileItem(id, image))
                    } else
                        if (file.extension != "gif")
                            list.add(FileItem(id, image))

            }

            photoCursor?.close()
            list.reverse()
            addPictureMap(title, list)
        }

        titleCursor?.close()
        titleItemSet.clear()
        titleItemList.clear()
        callback.invoke()
    }

    fun getFolderList(): List<FolderItem> {
        return PICTURE_MAP.entries
                .asSequence()
                .filter { it.value.isNotEmpty() }
                .map { it.key to it.value[0] }
                .map { FolderItem(it.first, it.second.imagePath) }
                .sortedBy { it.name.toLowerCase(Locale.ENGLISH) }
                .toList()
    }

    fun getImageList(title: String): MutableList<FileItem> {
        return PICTURE_MAP[title]?.toMutableList() ?: mutableListOf()
    }

    fun getImageList(): MutableList<FileItem> {
        val list = mutableListOf<FileItem>()
        PICTURE_MAP.values.forEach { list.addAll(it) }
        return list
    }

    fun isEmptyList() = PICTURE_MAP.isEmpty()

    fun getLimitMessage() = item.uiSetting.exceedLimitMessage.format(SelectedItem.getLimits())

    private fun Cursor.doWhile(action: () -> Unit) {
        this.use {
            if (this.moveToFirst()) {
                do {
                    action()
                } while (this.moveToNext())
            }
        }
    }

    private fun addPictureMap(title: String, list: MutableList<FileItem>) {
        PICTURE_MAP[title] = list
    }
}
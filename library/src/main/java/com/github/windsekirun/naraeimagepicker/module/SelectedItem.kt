package com.github.windsekirun.naraeimagepicker.module

import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.item.FileItem

/**
 * NaraeImagePicker
 * Class: SelectedItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

object SelectedItem {
    private val items: ArrayList<FileItem> = arrayListOf()
    private var limit = Constants.LIMIT_UNLIMITED

    val size: Int
        get() = items.size

    fun addItem(item: FileItem, listener: (Boolean) -> Unit) {
        if (limit != Constants.LIMIT_UNLIMITED && SelectedItem.items.size == limit) {
            listener.invoke(false)
            return
        }

        items.add(item)
        listener.invoke(true)
    }

    fun removeItem(item: FileItem) {
        items.remove(item)
    }

    fun contains(item: FileItem) = items.contains(item)

    fun setLimits(limit: Int) {
        this.limit = limit
    }

    fun getLimits() = limit

    fun getImageList(): ArrayList<String> = ArrayList<String>().apply {
        addAll(items.map { it.imagePath }.toList())
    }

    fun clear() {
        items.clear()
        items.trimToSize()
    }
}
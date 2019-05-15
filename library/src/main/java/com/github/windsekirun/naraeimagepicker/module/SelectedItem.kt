package com.github.windsekirun.naraeimagepicker.module

import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.item.ImageItem

/**
 * NaraePicker
 * Class: SelectedItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

object SelectedItem {
    private val items: ArrayList<ImageItem> = arrayListOf()
    private var count = Constants.LIMIT_UNLIMITED

    val size: Int
        get() = items.size

    fun addItem(item: ImageItem, listener: (Boolean) -> Unit) {
        if (count != Constants.LIMIT_UNLIMITED && SelectedItem.items.size == count) {
            listener.invoke(false)
            return
        }

        com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.add(item)
        listener.invoke(true)
    }

    fun removeItem(item: ImageItem) {
        com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.remove(item)
    }

    fun contains(item: ImageItem) = com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.contains(item)

    fun setLimits(limit: Int) {
        count = limit
    }

    fun getLimits() = count

    fun getImageList(): ArrayList<String> = ArrayList<String>().apply {
        addAll(items.map { it.imagePath }.toList())
    }

    fun isNotEmpty() = items.isNotEmpty()

    fun clear() {
        items.clear()
        items.trimToSize()
    }
}
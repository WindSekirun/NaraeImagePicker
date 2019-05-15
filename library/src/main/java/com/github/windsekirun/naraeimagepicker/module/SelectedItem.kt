package com.github.windsekirun.naraeimagepicker.module

/**
 * NaraeImagePicker
 * Class: SelectedItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

object SelectedItem {
    private val items: ArrayList<com.github.windsekirun.naraeimagepicker.item.ImageItem> = arrayListOf()
    private var count = com.github.windsekirun.naraeimagepicker.Constants.LIMIT_UNLIMITED

    fun addItem(item: com.github.windsekirun.naraeimagepicker.item.ImageItem, listener: (Boolean) -> Unit) {
        if (com.github.windsekirun.naraeimagepicker.module.SelectedItem.count != com.github.windsekirun.naraeimagepicker.Constants.LIMIT_UNLIMITED && com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.size == com.github.windsekirun.naraeimagepicker.module.SelectedItem.count) {
            listener.invoke(false)
            return
        }

        com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.add(item)
        listener.invoke(true)
    }

    fun removeItem(item: com.github.windsekirun.naraeimagepicker.item.ImageItem) {
        com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.remove(item)
    }

    fun contains(item: com.github.windsekirun.naraeimagepicker.item.ImageItem) = com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.contains(item)

    fun setLimits(limit: Int) {
        com.github.windsekirun.naraeimagepicker.module.SelectedItem.count = limit
    }

    fun getLimits() = com.github.windsekirun.naraeimagepicker.module.SelectedItem.count

    fun getImageList(): ArrayList<String> = ArrayList<String>().apply {
        addAll(com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.map { it.imagePath }.toList())
    }

    fun isNotEmpty() = com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.isNotEmpty()

    fun clear() {
        com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.clear()
        com.github.windsekirun.naraeimagepicker.module.SelectedItem.items.trimToSize()
    }
}
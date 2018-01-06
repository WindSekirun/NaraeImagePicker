package pyxis.uzuki.live.naraeimagepicker.module

import pyxis.uzuki.live.naraeimagepicker.item.ImageItem

/**
 * NaraeImagePicker
 * Class: SelectedItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

object SelectedItem {
    private val items: ArrayList<ImageItem> = arrayListOf()
    private var count = 5

    fun addItem(item: ImageItem, listener: (Boolean) -> Unit) {
        if (items.size == count) {
            listener.invoke(false)
            return
        }

        items.add(item)
        listener.invoke(true)
    }

    fun removeItem(item: ImageItem) {
        items.remove(item)
    }

    fun contains(item: ImageItem) = items.contains(item)

    fun setLimits(limit: Int) {
        this.count = limit
    }

    fun getLimits() = this.count

    fun getImageList() = items.map { it.imagePath }.toList()

    fun isNotEmpty() = items.isNotEmpty()
}
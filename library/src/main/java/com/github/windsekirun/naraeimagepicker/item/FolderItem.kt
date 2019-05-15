package com.github.windsekirun.naraeimagepicker.item

import java.io.Serializable

/**
 * NaraeImagePicker
 * Class: FolderItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

data class FolderItem(val name: String, val imagePath: String) : Serializable {

    override fun equals(other: Any?): Boolean {
        return name == (other as FolderItem).name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
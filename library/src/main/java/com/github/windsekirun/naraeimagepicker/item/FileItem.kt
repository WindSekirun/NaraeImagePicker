package com.github.windsekirun.naraeimagepicker.item

import java.io.Serializable

/**
 * NaraeImagePicker
 * Class: FolderItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

data class FileItem(val id: String, val imagePath: String) : Serializable {

    override fun equals(other: Any?): Boolean {
        return id == (other as FileItem).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
package pyxis.uzuki.live.naraeimagepicker.item

import java.io.Serializable

/**
 * NaraeImagePicker
 * Class: AlbumItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

data class ImageItem(val id: String, val imagePath: String) : Serializable {

    override fun equals(other: Any?): Boolean {
        return id == (other as ImageItem).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
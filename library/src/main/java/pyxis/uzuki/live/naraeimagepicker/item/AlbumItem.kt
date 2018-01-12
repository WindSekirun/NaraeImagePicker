package pyxis.uzuki.live.naraeimagepicker.item

import java.io.Serializable

/**
 * NaraeImagePicker
 * Class: AlbumItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

data class AlbumItem(val name: String, val imagePath: String) : Serializable {

    override fun equals(other: Any?): Boolean {
        return name == (other as AlbumItem).name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
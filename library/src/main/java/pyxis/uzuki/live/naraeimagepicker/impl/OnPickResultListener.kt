package pyxis.uzuki.live.naraeimagepicker.impl

import java.io.Serializable

/**
 * NaraeImagePicker
 * Class: OnPickResultListener
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

interface OnPickResultListener : Serializable {
    fun onSelect(resultCode: Int, imageList: ArrayList<String>)
}
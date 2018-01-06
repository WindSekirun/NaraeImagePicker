package pyxis.uzuki.live.naraeimagepicker.impl

/**
 * NaraeImagePicker
 * Class: OnPickResultListener
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

interface OnPickResultListener {
    fun onSelect(resultCode: Int, imageList: ArrayList<String>)
}
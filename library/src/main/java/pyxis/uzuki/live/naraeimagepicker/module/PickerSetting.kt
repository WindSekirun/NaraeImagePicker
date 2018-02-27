package pyxis.uzuki.live.naraeimagepicker.module

import pyxis.uzuki.live.naraeimagepicker.item.PickerSettingItem

/**
 * NaraeImagePicker
 * Class: PickerSetting
 * Created by Pyxis on 2018-02-27.
 *
 * Description:
 */
object PickerSetting {
    private lateinit var mItem: PickerSettingItem

    fun initialize(item: PickerSettingItem) {
        mItem = item
    }

    fun getItem() = mItem

    fun clear() {
        mItem.clear()
    }
}
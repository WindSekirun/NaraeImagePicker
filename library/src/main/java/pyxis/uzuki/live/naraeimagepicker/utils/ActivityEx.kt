package pyxis.uzuki.live.naraeimagepicker.utils

import android.support.v7.app.AppCompatActivity
import pyxis.uzuki.live.naraeimagepicker.item.PickerSettingItem

/**
 * Apply [PickerSettingItem] theme into Activity if configured.
 */
internal fun AppCompatActivity.applyCustomPickerTheme(settingItem: PickerSettingItem) {
    settingItem.uiSetting.themeResId?.let { themeResId ->
        setTheme(themeResId)
    }
}
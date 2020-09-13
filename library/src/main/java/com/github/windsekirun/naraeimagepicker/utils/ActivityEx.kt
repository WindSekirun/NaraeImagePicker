package com.github.windsekirun.naraeimagepicker.utils

import androidx.appcompat.app.AppCompatActivity
import com.github.windsekirun.naraeimagepicker.item.PickerSettingItem

/**
 * Apply [PickerSettingItem] theme into Activity if configured.
 */
internal fun AppCompatActivity.applyCustomPickerTheme(settingItem: PickerSettingItem) {
    settingItem.uiSetting.themeResId?.let { themeResId ->
        setTheme(themeResId)
    }
}
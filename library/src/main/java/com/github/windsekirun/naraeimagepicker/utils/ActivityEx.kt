package com.github.windsekirun.naraeimagepicker.utils

import android.support.v7.app.AppCompatActivity
import com.github.windsekirun.naraeimagepicker.item.PickerSettingItem

/**
 * Apply [PickerSettingItem] theme into Activity if configured.
 */
internal fun AppCompatActivity.applyCustomPickerTheme(settingItem: PickerSettingItem) {
    settingItem.uiSetting.themeResId?.let { themeResId ->
        setTheme(themeResId)
    }
}
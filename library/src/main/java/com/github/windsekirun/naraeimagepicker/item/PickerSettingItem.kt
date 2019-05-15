package com.github.windsekirun.naraeimagepicker.item

/**
 * NaraeImagePicker
 * Class: PickerSettingItem
 * Created by Pyxis on 2018-02-27.
 *
 * Description:
 */

class PickerSettingItem {
    var viewMode = com.github.windsekirun.naraeimagepicker.item.enumeration.ViewMode.FolderView
    var pickLimit = com.github.windsekirun.naraeimagepicker.Constants.LIMIT_UNLIMITED
    var disableZoomMode = false
    var uiSetting: com.github.windsekirun.naraeimagepicker.item.PickerSettingItem.UISetting = com.github.windsekirun.naraeimagepicker.item.PickerSettingItem.UISetting()

    class UISetting {
        var themeResId: Int? = null
        var pickerTitle = "Please select picture."
        var exceedLimitMessage = "Can\\'t select more than %s pictures"
        var enableUpInParentView = false
    }

    fun clear() {
        viewMode = com.github.windsekirun.naraeimagepicker.item.enumeration.ViewMode.FileView
        pickLimit = com.github.windsekirun.naraeimagepicker.Constants.LIMIT_UNLIMITED
        disableZoomMode = false
        uiSetting.enableUpInParentView = false
        uiSetting.pickerTitle = "Please select picture."
        uiSetting.exceedLimitMessage = "Can\\'t select more than %s pictures"
        uiSetting.themeResId = null
    }
}
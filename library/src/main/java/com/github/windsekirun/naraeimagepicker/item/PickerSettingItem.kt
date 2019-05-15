package com.github.windsekirun.naraeimagepicker.item

import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.item.enumeration.ViewMode

/**
 * NaraeImagePicker
 * Class: PickerSettingItem
 * Created by Pyxis on 2018-02-27.
 *
 * Description:
 */

class PickerSettingItem {
    var viewMode = ViewMode.FolderView
    var pickLimit = Constants.LIMIT_UNLIMITED
    var enableDetailMode = false
    var uiSetting: UISetting = UISetting()

    class UISetting {
        var themeResId: Int? = null
        var pickerTitle = "Please select picture."
        var exceedLimitMessage = "Can\\'t select more than %s pictures"
        var enableUpInParentView = false
        var folderSpanCount = 2
        var fileSpanCount = 3
    }

    fun clear() {
        viewMode = ViewMode.FileView
        pickLimit = Constants.LIMIT_UNLIMITED
        enableDetailMode = false
        uiSetting.enableUpInParentView = false
        uiSetting.pickerTitle = "Please select picture."
        uiSetting.exceedLimitMessage = "Can\\'t select more than %s pictures"
        uiSetting.themeResId = null
        uiSetting.folderSpanCount = 2
        uiSetting.fileSpanCount = 3
    }
}
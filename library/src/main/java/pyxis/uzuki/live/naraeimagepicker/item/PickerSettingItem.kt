package pyxis.uzuki.live.naraeimagepicker.item

import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.item.enumeration.ViewMode

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
    var disableZoomMode = false
    var uiSetting: UISetting = UISetting()

    class UISetting {
        var themeResId: Int? = null
        var pickerTitle = "Please select picture."
        var exceedLimitMessage = "Can\\'t select more than %s pictures"
        var enableUpInParentView = false
    }

    fun clear() {
        viewMode = ViewMode.FileView
        pickLimit = Constants.LIMIT_UNLIMITED
        disableZoomMode = false
        uiSetting.enableUpInParentView = false
        uiSetting.pickerTitle = "Please select picture."
        uiSetting.exceedLimitMessage = "Can\\'t select more than %s pictures"
        uiSetting.themeResId = null
    }
}
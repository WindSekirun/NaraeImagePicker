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
    var enableUpInParentView = false
    var pickerTitle = "Please select picture."
    var exceedLimitMessage = "Can\\'t select more than %s pictures"
    var disableZoomMode = false
    var themeResId: Int? = null

    fun clear() {
        viewMode = ViewMode.FileView
        pickLimit = Constants.LIMIT_UNLIMITED
        enableUpInParentView = false
        pickerTitle = "Please select picture."
        exceedLimitMessage = "Can\\'t select more than %s pictures"
        disableZoomMode = false
        themeResId = null
    }
}
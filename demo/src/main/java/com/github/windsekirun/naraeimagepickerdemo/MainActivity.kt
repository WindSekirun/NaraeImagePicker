package com.github.windsekirun.naraeimagepickerdemo

import android.arch.lifecycle.MutableLiveData
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.NaraeImagePicker
import com.github.windsekirun.naraeimagepicker.impl.OnPickResultListener
import com.github.windsekirun.naraeimagepicker.item.PickerSettingItem
import com.github.windsekirun.naraeimagepicker.item.enumeration.ViewMode
import com.github.windsekirun.naraeimagepicker.utils.toast
import com.github.windsekirun.naraeimagepickerdemo.databinding.MainActivityBinding


/**
 * NaraeImagePicker
 * Class: MainActivity
 * Created by Pyxis on 2019-05-15.
 *
 * Description:
 */

class MainActivity : AppCompatActivity() {
    val viewModeCheck = MutableLiveData<Int>()
    val detailModeEnable = MutableLiveData<Boolean>()

    private lateinit var binding: MainActivityBinding
    private val adapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.activity = this
        binding.lifecycleOwner = this

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
    }

    fun clickPick(view: View) {
        val settingItem = PickerSettingItem().apply {
            includeGif = false
            pickLimit = Constants.LIMIT_UNLIMITED
            viewMode = if (viewModeCheck.value == 0) ViewMode.FolderView else ViewMode.FileView
            enableDetailMode = detailModeEnable.value ?: false
            uiSetting.enableUpInParentView = true
            uiSetting.themeResId = R.style.AppTheme
            uiSetting.pickerTitle = "Custom Picker Title"
            uiSetting.fileSpanCount = 3
            uiSetting.folderSpanCount = 2
        }

        NaraeImagePicker.instance.start(this, settingItem, object : OnPickResultListener {
            override fun onSelect(resultCode: Int, imageList: ArrayList<String>) {
                if (resultCode != NaraeImagePicker.PICK_SUCCESS) {
                    toast("Failed to pick image")
                    return
                }

                adapter.setItems(imageList)
            }
        })
    }
}
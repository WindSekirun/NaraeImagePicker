package com.github.windsekirun.naraeimagepicker.fragment

import android.os.Bundle
import android.view.View
import com.github.windsekirun.naraeimagepicker.base.BaseFragment
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter
import com.github.windsekirun.naraeimagepicker.item.ImageItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import pyxis.uzuki.live.richutilskt.utils.toFile
import pyxis.uzuki.live.richutilskt.utils.toast

/**
 * NaraeImagePicker
 * Class: AllFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AllFragment : BaseFragment<ImageItem>() {
    private lateinit var adapter: ImageAdapter
    private val itemList = arrayListOf<ImageItem>()

    override fun getItemList() = itemList
    override fun getColumnCount(): Int = PickerSet.getSettingItem().uiSetting.fileSpanCount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ImageAdapter(itemList) { imageItem: ImageItem, _: Int, _: View ->
            onImageClick(imageItem)
        }
        recyclerView.adapter = adapter

        if (PickerSet.isEmptyList()) {
            runAsync { PickerSet.loadImageFirst(requireContext()) { bindList() } }
        } else {
            bindList()
        }
    }

    private fun bindList() {
        itemList.addAll(PickerSet.getImageList())
        itemList.sortedBy { it.imagePath.toFile().lastModified() }

        runOnUiThread {
            if (recyclerView != null) {
                recyclerView.notifyDataSetChanged()
            }
        }
    }

    private fun onImageClick(imageItem: ImageItem) {
        if (SelectedItem.contains(imageItem)) {
            SelectedItem.removeItem(imageItem)
        } else {
            addSelectedItem(imageItem)
        }
        sendEvent(ToolbarEvent("${SelectedItem.size} Selected", PickerSet.getSettingItem().uiSetting.enableUpInParentView))
        adapter.notifyDataSetChanged()
    }

    private fun addSelectedItem(item: ImageItem) {
        SelectedItem.addItem(item) {
            if (!it) this.activity?.toast(PickerSet.getLimitMessage())
        }
    }
}
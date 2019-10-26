package com.github.windsekirun.naraeimagepicker.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.base.BaseFragment
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter
import com.github.windsekirun.naraeimagepicker.item.FileItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import com.github.windsekirun.naraeimagepicker.utils.runOnUiThread
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.doAsync

/**
 * NaraeImagePicker
 * Class: FileFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class FileFragment : BaseFragment<FileItem>() {
    private lateinit var adapter: ImageAdapter
    private val itemList = arrayListOf<FileItem>()

    private var albumName = ""

    override fun getItemList() = itemList
    override fun getColumnCount(): Int = PickerSet.getSettingItem().uiSetting.fileSpanCount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumName = arguments?.getString(Constants.EXTRA_NAME) ?: ""
        sendEvent(ToolbarEvent(albumName, true))

        adapter = ImageAdapter(itemList) { imageItem, _, _ ->
            onImageClick(imageItem)
        }
        recyclerView.adapter = adapter
        doAsync { bindList() }
    }

    private fun bindList() {
        itemList.addAll(PickerSet.getImageList(albumName))

        runOnUiThread {
            recyclerView?.notifyDataSetChanged()
            sendEvent(ToolbarEvent("$albumName (${SelectedItem.size})", true))
        }
    }

    private fun onImageClick(fileItem: FileItem) {
        if (SelectedItem.contains(fileItem)) {
            SelectedItem.removeItem(fileItem)
        } else {
            addSelectedItem(fileItem)
        }
        sendEvent(ToolbarEvent("$albumName (${SelectedItem.size})", true))
        adapter.notifyDataSetChanged()
    }

    private fun addSelectedItem(item: FileItem) {
        SelectedItem.addItem(item) {
            if (!it) Toast.makeText(this.activity, PickerSet.getLimitMessage(), Toast.LENGTH_SHORT).show()
        }
    }
}
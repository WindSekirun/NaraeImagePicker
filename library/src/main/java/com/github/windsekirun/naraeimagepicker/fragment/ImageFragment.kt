package com.github.windsekirun.naraeimagepicker.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread

/**
 * NaraeImagePicker
 * Class: ImageFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageFragment : com.github.windsekirun.naraeimagepicker.base.BaseFragment<com.github.windsekirun.naraeimagepicker.item.ImageItem>() {
    private lateinit var mAdapter: com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter
    private val itemList = arrayListOf<com.github.windsekirun.naraeimagepicker.item.ImageItem>()
    private var selectCount = 0

    private var mAlbumName = ""

    override fun getItemList() = itemList
    override fun getItemKind() = com.github.windsekirun.naraeimagepicker.item.ImageItem::class.java.simpleName ?: ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAlbumName = arguments?.getString(com.github.windsekirun.naraeimagepicker.Constants.EXTRA_NAME) ?: ""
        sendEvent(com.github.windsekirun.naraeimagepicker.event.ToolbarEvent(mAlbumName, true))

        mAdapter = com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter(context as Context, itemList) { imageItem, _, _ ->
            onImageClick(imageItem)
        }
        recyclerView.adapter = mAdapter
        runAsync { bindList() }
    }

    private fun bindList() {
        itemList.addAll(com.github.windsekirun.naraeimagepicker.module.PickerSet.getImageList(mAlbumName))

        runOnUiThread {
            if (recyclerView != null) {
                recyclerView.notifyDataSetChanged()
            }
            sendEvent(com.github.windsekirun.naraeimagepicker.event.ToolbarEvent("$mAlbumName ($selectCount)", true))
        }
    }

    private fun onImageClick(imageItem: com.github.windsekirun.naraeimagepicker.item.ImageItem) {
        if (com.github.windsekirun.naraeimagepicker.module.SelectedItem.contains(imageItem)) {
            com.github.windsekirun.naraeimagepicker.module.SelectedItem.removeItem(imageItem)
            selectCount--
        } else {
            selectCount++
            addSelectedItem(imageItem)
        }
        sendEvent(com.github.windsekirun.naraeimagepicker.event.ToolbarEvent("$mAlbumName($selectCount)", true))
        mAdapter.notifyDataSetChanged()
    }

    private fun addSelectedItem(item: com.github.windsekirun.naraeimagepicker.item.ImageItem) {
        com.github.windsekirun.naraeimagepicker.module.SelectedItem.addItem(item) {
            if (!it) Toast.makeText(this.activity, com.github.windsekirun.naraeimagepicker.module.PickerSet.getLimitMessage(), Toast.LENGTH_SHORT).show()
        }
    }
}
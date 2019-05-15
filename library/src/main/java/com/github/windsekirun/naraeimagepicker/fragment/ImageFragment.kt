package com.github.windsekirun.naraeimagepicker.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.base.BaseFragment
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter
import com.github.windsekirun.naraeimagepicker.item.ImageItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread

/**
 * NaraePicker
 * Class: ImageFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageFragment : BaseFragment<ImageItem>() {
    private lateinit var adapter: ImageAdapter
    private val itemList = arrayListOf<ImageItem>()
    private var selectCount = 0

    private var albumName = ""

    override fun getItemList() = itemList
    override fun getItemKind() = ImageItem::class.java.simpleName ?: ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumName = arguments?.getString(Constants.EXTRA_NAME) ?: ""
        sendEvent(ToolbarEvent(albumName, true))

        adapter = ImageAdapter( itemList) { imageItem, _, _ ->
            onImageClick(imageItem)
        }
//        recyclerView.adapter = adapter
        runAsync { bindList() }
    }

    private fun bindList() {
        itemList.addAll(PickerSet.getImageList(albumName))

        runOnUiThread {
//            if (recyclerView != null) {
//                recyclerView.notifyDataSetChanged()
//            }
            sendEvent(ToolbarEvent("$albumName ($selectCount)", true))
        }
    }

    private fun onImageClick(imageItem: ImageItem) {
        if (SelectedItem.contains(imageItem)) {
            SelectedItem.removeItem(imageItem)
            selectCount--
        } else {
            selectCount++
            addSelectedItem(imageItem)
        }
        sendEvent(ToolbarEvent("$albumName($selectCount)", true))
        adapter.notifyDataSetChanged()
    }

    private fun addSelectedItem(item: ImageItem) {
        SelectedItem.addItem(item) {
            if (!it) Toast.makeText(this.activity, PickerSet.getLimitMessage(), Toast.LENGTH_SHORT).show()
        }
    }
}
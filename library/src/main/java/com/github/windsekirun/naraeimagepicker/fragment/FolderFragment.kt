package com.github.windsekirun.naraeimagepicker.fragment

import android.os.Bundle
import android.view.View
import com.github.windsekirun.naraeimagepicker.base.BaseFragment
import com.github.windsekirun.naraeimagepicker.fragment.adapter.AlbumAdapter
import com.github.windsekirun.naraeimagepicker.item.FolderItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread

/**
 * NaraeImagePicker
 * Class: FolderFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class FolderFragment : BaseFragment<FolderItem>() {
    private lateinit var adapter: AlbumAdapter

    private val itemList = arrayListOf<FolderItem>()

    override fun getItemList() = itemList
    override fun getColumnCount(): Int = PickerSet.getSettingItem().uiSetting.folderSpanCount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AlbumAdapter(itemList)
        recyclerView.adapter = adapter

        if (PickerSet.isEmptyList()) {
            runAsync { PickerSet.loadImageFirst(requireContext()) { bindList() } }
        } else {
            bindList()
        }
    }

    private fun bindList() {
        itemList.addAll(PickerSet.getFolderList())
        runOnUiThread {
            recyclerView?.notifyDataSetChanged()
        }
    }
}
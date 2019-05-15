package com.github.windsekirun.naraeimagepicker.fragment

import android.os.Bundle
import android.view.View
import com.github.windsekirun.naraeimagepicker.base.BaseFragment
import com.github.windsekirun.naraeimagepicker.fragment.adapter.AlbumAdapter
import com.github.windsekirun.naraeimagepicker.item.AlbumItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread

/**
 * NaraeImagePicker
 * Class: AlbumFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AlbumFragment : BaseFragment<AlbumItem>() {
    private lateinit var adapter: AlbumAdapter

    private val itemList = arrayListOf<AlbumItem>()

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
            if (recyclerView != null) {
                recyclerView.notifyDataSetChanged()
            }
        }
    }
}
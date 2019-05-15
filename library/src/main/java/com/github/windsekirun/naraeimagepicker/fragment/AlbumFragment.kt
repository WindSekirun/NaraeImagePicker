package com.github.windsekirun.naraeimagepicker.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
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

class AlbumFragment : com.github.windsekirun.naraeimagepicker.base.BaseFragment<com.github.windsekirun.naraeimagepicker.item.AlbumItem>() {
    private lateinit var adapter: com.github.windsekirun.naraeimagepicker.fragment.adapter.AlbumAdapter
    private val itemList = arrayListOf<com.github.windsekirun.naraeimagepicker.item.AlbumItem>()

    override fun getItemList() = itemList
    override fun getItemKind() = com.github.windsekirun.naraeimagepicker.item.AlbumItem::class.java.simpleName ?: ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = com.github.windsekirun.naraeimagepicker.fragment.adapter.AlbumAdapter(context as Context, itemList)
        recyclerView.adapter = adapter

        if (com.github.windsekirun.naraeimagepicker.module.PickerSet.isEmptyList()) {
            runAsync { com.github.windsekirun.naraeimagepicker.module.PickerSet.loadImageFirst(context!!) { bindList() } }
        } else {
            bindList()
        }
    }

    private fun bindList() {
        itemList.addAll(com.github.windsekirun.naraeimagepicker.module.PickerSet.getFolderList())
        runOnUiThread {
            if (recyclerView != null) {
                recyclerView.notifyDataSetChanged()
            }
        }
    }
}
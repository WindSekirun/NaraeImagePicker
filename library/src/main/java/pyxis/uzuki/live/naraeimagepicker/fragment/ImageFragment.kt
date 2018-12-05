package pyxis.uzuki.live.naraeimagepicker.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.base.BaseFragment
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.fragment.adapter.ImageAdapter
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.module.PickerSet
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread

/**
 * NaraeImagePicker
 * Class: ImageFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageFragment : BaseFragment<ImageItem>() {
    private lateinit var mAdapter: ImageAdapter
    private val itemList = arrayListOf<ImageItem>()

    private var mAlbumName = ""

    override fun getItemList() = itemList
    override fun getItemKind() = ImageItem::class.java.simpleName ?: ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAlbumName = arguments?.getString(Constants.EXTRA_NAME) ?: ""
        sendEvent(ToolbarEvent(mAlbumName, true))

        mAdapter = ImageAdapter(context as Context, itemList)
        recyclerView.adapter = mAdapter
        runAsync { bindList() }
    }

    private fun bindList() {
        itemList.addAll(PickerSet.getImageList(mAlbumName))

        runOnUiThread {
            if (recyclerView != null) {
                recyclerView.notifyDataSetChanged()
            }
            sendEvent(ToolbarEvent("$mAlbumName (${itemList.size})", true))
        }
    }
}
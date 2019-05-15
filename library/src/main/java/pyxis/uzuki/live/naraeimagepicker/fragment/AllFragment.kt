package pyxis.uzuki.live.naraeimagepicker.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.naraeimagepicker.base.BaseFragment
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.fragment.adapter.ImageAdapter
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.module.PickerSet
import pyxis.uzuki.live.naraeimagepicker.module.SelectedItem
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import pyxis.uzuki.live.richutilskt.utils.toFile

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
    private var selectCount = 0

    override fun getItemList() = itemList
    override fun getItemKind() = ImageItem::class.java.simpleName ?: ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ImageAdapter(context as Context, itemList) { imageItem: ImageItem, _: Int, _: View ->
            onImageClick(imageItem)
        }
        recyclerView.adapter = adapter

        if (PickerSet.isEmptyList()) {
            runAsync { PickerSet.loadImageFirst(context!!) { bindList() } }
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
            selectCount--
        } else {
            selectCount++
            addSelectedItem(imageItem)
        }
        sendEvent(ToolbarEvent("$selectCount Selected", true))
        adapter.notifyDataSetChanged()
    }

    private fun addSelectedItem(item: ImageItem) {
        SelectedItem.addItem(item) {
            if (!it) Toast.makeText(this.activity, PickerSet.getLimitMessage(), Toast.LENGTH_SHORT).show()
        }
    }
}
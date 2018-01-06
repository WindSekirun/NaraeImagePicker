package pyxis.uzuki.live.naraeimagepicker.fragment

import android.app.Fragment
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_image_row.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.base.BaseFragment
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.module.SelectedItem
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.naraeimagepicker.widget.AdjustableGridItemDecoration
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

class AllFragment : BaseFragment() {

}
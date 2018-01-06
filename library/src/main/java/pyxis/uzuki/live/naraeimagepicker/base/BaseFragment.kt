package pyxis.uzuki.live.naraeimagepicker.base

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.widget.AdjustableGridItemDecoration


/**
 * NaraeImagePicker
 * Class: BaseFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

abstract class BaseFragment<T> : Fragment() {
    private lateinit var mRootView: View

    abstract fun getItemList() : ArrayList<T>

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_list, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendEvent(ToolbarEvent(getString(R.string.narae_image_picker_album_title)))

        val rectF = AdjustableGridItemDecoration.getRectFObject(activity)

        recyclerView.mEmptyView = containerEmpty
        recyclerView.mLoadingView = progressBar
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(AdjustableGridItemDecoration(rectF, getItemList(), 3))
    }

    override fun onDestroyView() {
        if (mRootView.parent != null) {
            (mRootView.parent as ViewGroup).removeView(mRootView)
        }
        super.onDestroyView()
    }

    fun <T> sendEvent(event: T) {
        EventBus.getDefault().post(event)
    }
}
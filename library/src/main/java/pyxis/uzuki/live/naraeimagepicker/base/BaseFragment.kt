package pyxis.uzuki.live.naraeimagepicker.base

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R


/**
 * NaraeImagePicker
 * Class: BaseFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

open class BaseFragment : Fragment() {
    private lateinit var mRootView: View

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_list, container, false)
        return mRootView
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
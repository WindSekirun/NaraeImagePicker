package pyxis.uzuki.live.naraeimagepicker.folder

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pyxis.uzuki.live.naraeimagepicker.R
import android.app.Activity
import android.content.Context
import pyxis.uzuki.live.naraeimagepicker.impl.OnItemSelectListener


/**
 * NaraeImagePicker
 * Class: BaseFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

open class BaseFragment : Fragment() {
    private lateinit var mRootView: View
    lateinit var mCallback: OnItemSelectListener

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle): View? {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false)
        return mRootView
    }

    override fun onDestroyView() {
        if (mRootView.parent != null) {
            (mRootView.parent as ViewGroup).removeView(mRootView)
        }
        super.onDestroyView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as? Activity
        if (activity != null) {
            onAttachProcess(activity)
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        // deprecated in Android M, but some device need this methods.
        // so pass [activity] to onAttachProcess
        onAttachProcess(activity)
    }

    private fun onAttachProcess(activity: Activity) {
        try {
            mCallback = activity as OnItemSelectListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener")
        }
    }
}
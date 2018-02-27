@file:JvmName("NaraeImagePicker")

package pyxis.uzuki.live.naraeimagepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import pyxis.uzuki.live.naraeimagepicker.activity.NaraePickerActivity
import pyxis.uzuki.live.naraeimagepicker.impl.OnPickResultListener
import pyxis.uzuki.live.naraeimagepicker.item.PickerSettingItem
import pyxis.uzuki.live.naraeimagepicker.module.PickerSetting
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread

class NaraeImagePicker private constructor() {
    private val requestCode = 72

    private fun getActivity(context: Context): Activity? {
        var c = context

        while (c is ContextWrapper) {
            if (c is Activity) {
                return c
            }
            c = c.baseContext
        }
        return null
    }

    @JvmOverloads
    fun start(context: Context, item: PickerSettingItem = PickerSettingItem(), pickResultListener: OnPickResultListener) {
        requestStart(context, item, pickResultListener)
    }

    @SuppressLint("ValidFragment")
    private fun requestStart(context: Context, item: PickerSettingItem, pickResultListener: OnPickResultListener) {
        val fm = getActivity(context)?.fragmentManager
        val intent = Intent(getActivity(context), NaraePickerActivity::class.java)
        val fragment = ResultFragment(fm as FragmentManager, pickResultListener)
        fm.beginTransaction().add(fragment, "FRAGMENT_TAG").commitAllowingStateLoss()
        fm.executePendingTransactions()

        PickerSetting.initialize(item)

        fragment.startActivityForResult(intent, requestCode)
    }

    @SuppressLint("ValidFragment")
    class ResultFragment() : Fragment() {
        var mFragmentManager: FragmentManager? = null
        lateinit var mCallback: OnPickResultListener

        constructor(fm: FragmentManager, callback: OnPickResultListener) : this() {
            this.mFragmentManager = fm
            this.mCallback = callback
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                val imageList = data?.getStringArrayListExtra(Constants.EXTRA_IMAGE_LIST)
                imageList?.let { runOnUiThread { mCallback.onSelect(PICK_SUCCESS, it) } }
            } else {
                runOnUiThread { mCallback.onSelect(PICK_FAILED, arrayListOf()) }
            }

            mFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    companion object {
        @JvmField
        var instance: NaraeImagePicker = NaraeImagePicker()

        @JvmField
        val PICK_SUCCESS = 1
        @JvmField
        val PICK_FAILED = 0
    }

}
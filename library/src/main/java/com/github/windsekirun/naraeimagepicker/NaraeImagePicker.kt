@file:JvmName("NaraeImagePicker")

package com.github.windsekirun.naraeimagepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity
import com.github.windsekirun.naraeimagepicker.impl.OnPickResultListener
import com.github.windsekirun.naraeimagepicker.item.PickerSettingItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.utils.runOnUiThread

class NaraeImagePicker private constructor() {
    private val requestCode = 72

    private fun getActivity(context: Context): FragmentActivity? {
        var c = context

        while (c is ContextWrapper) {
            if (c is FragmentActivity) {
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
        val intent = Intent(getActivity(context), NaraePickerActivity::class.java)

        val fragmentManager = getActivity(context)?.supportFragmentManager
        val fragment = ResultFragment(fragmentManager as FragmentManager, pickResultListener)
        fragmentManager.beginTransaction().add(fragment, "FRAGMENT_TAG").commitAllowingStateLoss()
        fragmentManager.executePendingTransactions()

        PickerSet.setSettingItem(item)

        fragment.startActivityForResult(intent, requestCode)
    }

    @SuppressLint("ValidFragment")
    class ResultFragment() : Fragment() {
        private var _fragmentManager: FragmentManager? = null
        private lateinit var _callback: OnPickResultListener

        constructor(fm: FragmentManager, callback: OnPickResultListener) : this() {
            this._fragmentManager = fm
            this._callback = callback
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            runOnUiThread {
                if (resultCode == Activity.RESULT_OK) {
                    val imageList = data?.getStringArrayListExtra(Constants.EXTRA_IMAGE_LIST)
                    imageList?.let { _callback.onSelect(PICK_SUCCESS, it) }
                } else {
                    _callback.onSelect(PICK_FAILED, arrayListOf())
                }
            }

            _fragmentManager?.beginTransaction()?.remove(this)?.commit()
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
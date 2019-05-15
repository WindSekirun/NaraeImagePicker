@file:JvmName("NaraeImagePicker")

package com.github.windsekirun.naraeimagepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
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
    fun start(context: Context, item: com.github.windsekirun.naraeimagepicker.item.PickerSettingItem = com.github.windsekirun.naraeimagepicker.item.PickerSettingItem(), pickResultListener: com.github.windsekirun.naraeimagepicker.impl.OnPickResultListener) {
        requestStart(context, item, pickResultListener)
    }

    @SuppressLint("ValidFragment")
    private fun requestStart(context: Context, item: com.github.windsekirun.naraeimagepicker.item.PickerSettingItem, pickResultListener: com.github.windsekirun.naraeimagepicker.impl.OnPickResultListener) {
        val fm = getActivity(context)?.fragmentManager
        val intent = Intent(getActivity(context), com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity::class.java)
        val fragment = com.github.windsekirun.naraeimagepicker.NaraeImagePicker.ResultFragment(fm as FragmentManager, pickResultListener)
        fm.beginTransaction().add(fragment, "FRAGMENT_TAG").commitAllowingStateLoss()
        fm.executePendingTransactions()

        com.github.windsekirun.naraeimagepicker.module.PickerSet.setSettingItem(item)

        fragment.startActivityForResult(intent, requestCode)
    }

    @SuppressLint("ValidFragment")
    class ResultFragment() : Fragment() {
        var mFragmentManager: FragmentManager? = null
        lateinit var mCallback: com.github.windsekirun.naraeimagepicker.impl.OnPickResultListener

        constructor(fm: FragmentManager, callback: com.github.windsekirun.naraeimagepicker.impl.OnPickResultListener) : this() {
            this.mFragmentManager = fm
            this.mCallback = callback
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            runOnUiThread {
                if (resultCode == Activity.RESULT_OK) {
                    val imageList = data?.getStringArrayListExtra(com.github.windsekirun.naraeimagepicker.Constants.EXTRA_IMAGE_LIST)
                    imageList?.let { mCallback.onSelect(com.github.windsekirun.naraeimagepicker.NaraeImagePicker.Companion.PICK_SUCCESS, it) }
                } else {
                    mCallback.onSelect(com.github.windsekirun.naraeimagepicker.NaraeImagePicker.Companion.PICK_FAILED, arrayListOf())
                }
            }

            mFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    companion object {
        @JvmField
        var instance: com.github.windsekirun.naraeimagepicker.NaraeImagePicker = com.github.windsekirun.naraeimagepicker.NaraeImagePicker()

        @JvmField
        val PICK_SUCCESS = 1
        @JvmField
        val PICK_FAILED = 0
    }

}
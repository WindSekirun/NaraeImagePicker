@file:JvmName("NaraePicker")

package com.github.windsekirun.naraeimagepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity
import com.github.windsekirun.naraeimagepicker.impl.OnPickResultListener
import com.github.windsekirun.naraeimagepicker.item.PickerSettingItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread

class NaraePicker private constructor() {
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
        val fm = getActivity(context)?.supportFragmentManager
        val intent = Intent(getActivity(context), NaraePickerActivity::class.java)
        val fragment = ResultFragment(fm as FragmentManager, pickResultListener)
        fm.beginTransaction().add(fragment, "FRAGMENT_TAG").commitAllowingStateLoss()
        fm.executePendingTransactions()

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
        var instance: NaraePicker = NaraePicker()

        @JvmField
        val PICK_SUCCESS = 1
        @JvmField
        val PICK_FAILED = 0
    }

}
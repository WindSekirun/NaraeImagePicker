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
import pyxis.uzuki.live.richutilskt.impl.F2

class NaraeImagePicker private constructor() {
    private val requestCode = -72

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

    @JvmOverloads fun start(context: Context, limit: Int = 5, callback: (Int, List<String>) -> Unit) =
            requestStart(context, limit, callback)

    @JvmOverloads fun start(context: Context, limit: Int = 5, callback: F2<Int, List<String>>) =
            requestStart(context, limit, { result, list -> callback.invoke(result, list) })

    @SuppressLint("ValidFragment")
    private fun requestStart(context: Context, limit: Int = 5, callback: (Int, List<String>) -> Unit) {

        val fm = getActivity(context)?.fragmentManager

        val intent = Intent(context, NaraePickerActivity::class.java)
        intent.putExtra(Constants.EXTRA_LIMIT, limit)

        val fragment = ResultFragment(fm as FragmentManager, callback)
        fm.beginTransaction().add(fragment, "FRAGMENT_TAG").commitAllowingStateLoss()
        fm.executePendingTransactions()

        fragment.startActivityForResult(intent, requestCode)
    }

    @SuppressLint("ValidFragment")
    class ResultFragment() : Fragment() {
        var fm: FragmentManager? = null
        var callback: ((Int, List<String>) -> Unit)? = null

        constructor(fm: FragmentManager, callback: (Int, List<String>) -> Unit) : this() {
            this.fm = fm
            this.callback = callback
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                val imageList = data?.getStringArrayListExtra(Constants.EXTRA_IMAGE_LIST)
                imageList?.let { callback?.invoke(PICK_SUCCESS, it) }
            } else {
                callback?.invoke(PICK_FAILED, arrayListOf())
            }

            fm?.beginTransaction()?.remove(this)?.commit()
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
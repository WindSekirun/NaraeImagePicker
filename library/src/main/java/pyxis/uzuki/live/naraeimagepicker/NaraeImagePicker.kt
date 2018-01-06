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
    fun start(context: Context, limit: Int = Constants.LIMIT_UNLIMITED, callback: OnPickResultListener) =
            requestStart(context, limit, false, { result, list -> callback.onSelect(result, list) })

    fun start(context: Context, limit: Int = Constants.LIMIT_UNLIMITED, callback: (Int, ArrayList<String>) -> Unit) =
            requestStart(context, limit, false, callback)

    @JvmOverloads
    fun startAll(context: Context, limit: Int = Constants.LIMIT_UNLIMITED, callback: OnPickResultListener) =
            requestStart(context, limit, true, { result, list -> callback.onSelect(result, list) })

    fun startAll(context: Context, limit: Int = Constants.LIMIT_UNLIMITED, callback: (Int, ArrayList<String>) -> Unit) =
            requestStart(context, limit, true, callback)

    @SuppressLint("ValidFragment")
    private fun requestStart(context: Context, limit: Int, allMode: Boolean = false, callback: (Int, ArrayList<String>) -> Unit) {

        val fm = getActivity(context)?.fragmentManager

        val intent = Intent(getActivity(context), NaraePickerActivity::class.java)
        intent.putExtra(Constants.EXTRA_LIMIT, limit)
        intent.putExtra(Constants.EXTRA_REQUEST_ALL_MODE, allMode)

        val fragment = ResultFragment(fm as FragmentManager, callback)
        fm.beginTransaction().add(fragment, "FRAGMENT_TAG").commitAllowingStateLoss()
        fm.executePendingTransactions()

        fragment.startActivityForResult(intent, requestCode)
    }

    @SuppressLint("ValidFragment")
    class ResultFragment() : Fragment() {
        var fm: FragmentManager? = null
        var callback: ((Int, ArrayList<String>) -> Unit)? = null

        constructor(fm: FragmentManager, callback: (Int, ArrayList<String>) -> Unit) : this() {
            this.fm = fm
            this.callback = callback
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                val imageList = data?.getStringArrayListExtra(Constants.EXTRA_IMAGE_LIST)
                imageList?.let { runOnUiThread { callback?.invoke(PICK_SUCCESS, it) } }
            } else {
                runOnUiThread { callback?.invoke(PICK_FAILED, arrayListOf()) }
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
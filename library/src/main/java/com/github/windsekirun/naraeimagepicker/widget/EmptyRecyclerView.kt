package com.github.windsekirun.naraeimagepicker.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * NaraeImagePicker
 * Class: EmptyRecyclerView
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

open class EmptyRecyclerView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs) {
    var emptyView: View? = null
    var loadingView: View? = null

    private var observer: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            if (adapter == null) {
                return
            }

            if (emptyView == null) {
                throw NullPointerException("Empty view in RecyclerView is null-state")
            }

            loadingView!!.visibility = View.GONE // whatever state is changed, loadingView need to gone.

            val newStateEmptyView = if (adapter?.itemCount == 0) View.VISIBLE else View.GONE
            val newStateRecyclerView = if (adapter?.itemCount == 0) View.GONE else View.VISIBLE

            emptyView?.visibility = newStateEmptyView
            this@EmptyRecyclerView.visibility = newStateRecyclerView
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)

        if (loadingView == null) {
            throw NullPointerException("Loading view in RecyclerView is null-state")
        }

        loadingView?.visibility = View.VISIBLE
    }

    fun notifyDataSetChanged() {
        adapter?.registerAdapterDataObserver(observer)
        adapter?.notifyDataSetChanged()
        observer.onChanged()
    }
}


package pyxis.uzuki.live.naraeimagepicker.widget

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

open class EmptyRecyclerView(context: Context, private val attrs: AttributeSet? = null) : RecyclerView(context, attrs) {
    var mEmptyView: View? = null
    private var mEmptyObserver: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            val adapter = adapter
            if (adapter == null) {
                return
            }

            if (mEmptyView == null) {
                throw NullPointerException("Empty view in RecyclerView is null-state")
            }

            val newStateEmptyView = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
            val newStateRecyclerView = if (adapter.itemCount == 0) View.GONE else View.VISIBLE

            mEmptyView!!.visibility = newStateEmptyView
            this@EmptyRecyclerView.visibility = newStateRecyclerView
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(mEmptyObserver)
        mEmptyObserver.onChanged()
    }
}


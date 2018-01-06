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
    var mLoadingView: View? = null

    private var mObserver: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            if (adapter == null) {
                return
            }

            if (mEmptyView == null) {
                throw NullPointerException("Empty view in RecyclerView is null-state")
            }

            mLoadingView!!.visibility = View.GONE // whatever state is changed, mLoadingView need to gone.

            val newStateEmptyView = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
            val newStateRecyclerView = if (adapter.itemCount == 0) View.GONE else View.VISIBLE

            mEmptyView!!.visibility = newStateEmptyView
            this@EmptyRecyclerView.visibility = newStateRecyclerView
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)

        if (mLoadingView == null) {
            throw NullPointerException("Loading view in RecyclerView is null-state")
        }

        mLoadingView!!.visibility = View.VISIBLE
    }

    fun notifyDataSetChanged() {
        adapter?.registerAdapterDataObserver(mObserver)
        mObserver.onChanged()
    }
}


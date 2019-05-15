package com.github.windsekirun.naraeimagepicker.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.support.v7.widget.RecyclerView
import android.view.View
import pyxis.uzuki.live.richutilskt.utils.dip2px

/**
 * NaraeImagePicker
 * Class: AdjustableGridItemDecoration
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */
class AdjustableGridItemDecoration(private val spacing: RectF, private val collection: List<*>, private val column: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val row = position / column
        val cell = position % column

        val totalRow = collection.size / column

        if (position >= column) {
            outRect.top = spacing.top.toInt() / 2
        }

        if (totalRow != row) {
            outRect.bottom = spacing.bottom.toInt() / 2
        }

        outRect.left = spacing.left.toInt() / 2

        if (cell <= column - 1) {
            outRect.right = spacing.right.toInt() / 2
        }
    }

    companion object {

        @JvmStatic
        fun Context.getRectFObject(left: Int, top: Int, right: Int, bottom: Int): RectF {
            return RectF(dip2px(left).toFloat(), dip2px(top).toFloat(), dip2px(right).toFloat(), dip2px(bottom).toFloat())
        }

        @JvmStatic
        fun getRectFObject(context: Context): RectF {
            return context.getRectFObject(1, 1, 1, 1)
        }
    }
}
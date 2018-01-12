package pyxis.uzuki.live.naraeimagepicker.utils;

import android.os.SystemClock
import android.util.Log
import java.util.*

class TimeLogger(tag: String = "", label: String = "") {

    private var mTag: String = ""
    private var mLabel: String = ""
    private var mParts: ArrayList<Long> = arrayListOf()
    private var mPartLabels: ArrayList<String> = arrayListOf()

    init {
        reset(tag, label)
    }

    fun reset(tag: String, label: String) {
        mTag = tag
        mLabel = label
        reset()
    }

    fun reset() {
        if (mParts.isNotEmpty()) {
            mParts.clear()
            mPartLabels.clear()
        } else {
            mParts = ArrayList()
            mPartLabels = ArrayList()
        }
        addPart("")
    }

    fun addPart(partLabel: String) {
        val now = SystemClock.elapsedRealtime()
        mParts.add(now)
        mPartLabels.add(partLabel)
    }

    fun println() {
        Log.d(mTag, mLabel + ": begin")
        val first = mParts[0]
        var now = first
        for (i in 1 until mParts.size) {
            now = mParts[i]
            val label = mPartLabels[i]
            val prev = mParts[i - 1]

            Log.d(mTag, mLabel + ":      " + (now - prev) + " ms, " + label)
        }
        Log.d(mTag, mLabel + ": end, " + (now - first) + " ms")
    }
}
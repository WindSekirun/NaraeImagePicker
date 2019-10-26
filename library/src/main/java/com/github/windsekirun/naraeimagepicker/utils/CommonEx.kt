package com.github.windsekirun.naraeimagepicker.utils

import android.app.Activity
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

/**
 * NaraeImagePicker
 * Class: CommonEx
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

fun Cursor.getColumnString(columnName: String) = this.getString(this.getColumnIndex(columnName))

fun Cursor.getColumnInt(columnName: String) = this.getInt(this.getColumnIndex(columnName))

fun Cursor.getColumnLong(columnName: String) = this.getLong(this.getColumnIndex(columnName))

inline fun catchAll(action: () -> Unit) {
    try {
        action()
    } catch (t: Throwable) {
        Log.e("NaraeImagePicker", "Catch an exception. ${t.message}", t)
    }
}

fun Activity?.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun runOnUiThread(action: () -> Unit) = Handler(Looper.getMainLooper()).post(Runnable(action))

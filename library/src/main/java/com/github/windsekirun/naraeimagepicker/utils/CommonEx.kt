package com.github.windsekirun.naraeimagepicker.utils

import android.database.Cursor
import android.util.Log

/**
 * NaraePicker
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
        Log.e("NaraePicker", "Catch an exception. ${t.message}", t)
    }
}
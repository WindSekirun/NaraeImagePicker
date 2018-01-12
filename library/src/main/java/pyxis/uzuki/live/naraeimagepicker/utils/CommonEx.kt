package pyxis.uzuki.live.naraeimagepicker.utils

import android.database.Cursor

/**
 * NaraeImagePicker
 * Class: CommonEx
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

/**
 * 해당 커서에 위치한 [columnName] 을 기반으로 문자열 값을 가져옴
 */
fun Cursor.getColumnString(columnName: String) = this.getString(this.getColumnIndex(columnName))

fun Cursor.getColumnInt(columnName: String) = this.getInt(this.getColumnIndex(columnName))

fun Cursor.getColumnLong(columnName: String) = this.getLong(this.getColumnIndex(columnName))

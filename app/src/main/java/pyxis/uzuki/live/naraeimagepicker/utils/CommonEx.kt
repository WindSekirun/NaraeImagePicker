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

/**
 * 리스트 초기화
 */
fun <T> ArrayList<T>.clearAndTrim() {
    this.clear()
    this.trimToSize()
}
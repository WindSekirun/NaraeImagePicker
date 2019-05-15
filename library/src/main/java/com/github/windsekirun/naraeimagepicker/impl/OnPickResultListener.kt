package com.github.windsekirun.naraeimagepicker.impl

import java.io.Serializable

/**
 * NaraePicker
 * Class: OnPickResultListener
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

interface OnPickResultListener : Serializable {
    fun onSelect(resultCode: Int, imageList: ArrayList<String>)
}
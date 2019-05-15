package com.github.windsekirun.naraeimagepickerdemo.binding

import android.databinding.*
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup

/**
 * NaraePicker Demo
 * Class: RadioGroupBindAdapter
 * Created by Pyxis on 7/7/18.
 *
 * Description:
 */
@BindingMethods(
        BindingMethod(type = RadioGroup::class, attribute = "android:onRadioChanged", method = "setOnCheckedChangeListener")
)
object RadioGroupBindAdapter {

    @BindingAdapter("checkedPosition")
    @JvmStatic
    fun setCheckPosition(radioGroup: RadioGroup, position: Int) {
        if (com.github.windsekirun.naraeimagepickerdemo.binding.RadioGroupBindAdapter.getRadioGroupIndex(radioGroup) != position) {
            val childAt = radioGroup.getChildAt(position) as? RadioButton
            childAt?.isChecked = true
        }
    }

    @BindingAdapter("checkedPositionAttrChanged")
    @JvmStatic
    fun setInverseBindingListener(radioGroup: RadioGroup, listener: InverseBindingListener) {
        radioGroup.setOnCheckedChangeListener { _, _ ->
            listener.onChange()
        }
    }

    @InverseBindingAdapter(attribute = "checkedPosition", event = "checkedPositionAttrChanged")
    @JvmStatic
    fun getCheckedPosition(radioGroup: RadioGroup): Int {
        return com.github.windsekirun.naraeimagepickerdemo.binding.RadioGroupBindAdapter.getRadioGroupIndex(radioGroup)
    }

    @JvmStatic
    private fun getRadioGroupIndex(group: RadioGroup): Int {
        val radioButtonID = group.checkedRadioButtonId
        val radioButton = group.findViewById<View>(radioButtonID)
        return group.indexOfChild(radioButton)
    }

}
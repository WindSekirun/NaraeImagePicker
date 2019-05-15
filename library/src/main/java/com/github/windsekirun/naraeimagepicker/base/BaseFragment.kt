package com.github.windsekirun.naraeimagepicker.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.widget.AdjustableGridItemDecoration
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.naraeimagepicker.R

/**
 * NaraePicker
 * Class: BaseFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

abstract class BaseFragment<T : Any> : Fragment() {
    private lateinit var mRootView: View

    abstract fun getItemList(): ArrayList<T>
    abstract fun getItemKind(): String

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_list, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendEvent(ToolbarEvent(PickerSet.getSettingItem().uiSetting.pickerTitle,
                PickerSet.getSettingItem().uiSetting.enableUpInParentView))

        val rectF = AdjustableGridItemDecoration.getRectFObject(context as Context)
        val column = if (getItemKind() == com.github.windsekirun.naraeimagepicker.item.ImageItem::class.java.simpleName) 3 else 2

//        recyclerView.apply {
//            layoutManager = GridLayoutManager(activity, column)
//            mEmptyView = containerEmpty
//            mLoadingView = progressBar
//            setHasFixedSize(true)
//            addItemDecoration(AdjustableGridItemDecoration(rectF, getItemList(), column))
//        }
    }

    override fun onDestroyView() {
        if (mRootView.parent != null) (mRootView.parent as ViewGroup).removeView(mRootView)
        super.onDestroyView()
    }

    fun <T> sendEvent(event: T) {
        EventBus.getDefault().post(event)
    }
}
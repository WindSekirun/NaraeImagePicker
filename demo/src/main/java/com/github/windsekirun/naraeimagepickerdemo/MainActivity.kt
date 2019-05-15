package com.github.windsekirun.naraeimagepickerdemo

import android.arch.lifecycle.MutableLiveData
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pyxis.uzuki.live.naraeimagepickerdemo.databinding.MainActivityBinding

/**
 * NaraePicker
 * Class: MainActivity
 * Created by Pyxis on 2019-05-15.
 *
 * Description:
 */

class MainActivity : AppCompatActivity() {
    val viewModeCheck = MutableLiveData<Boolean>()
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)


    }
}
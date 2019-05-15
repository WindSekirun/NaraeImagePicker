package com.github.windsekirun.naraeimagepicker.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.utils.applyCustomPickerTheme
import pyxis.uzuki.live.naraeimagepicker.utils.catchAll
import pyxis.uzuki.live.richutilskt.utils.RPermission

class NaraePickerActivity : AppCompatActivity() {
    private var mLastFragmentMode = com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.Album
    private var mRequestFileViewMode = false

    private enum class FragmentMode {
        Album, Image, All
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyCustomPickerTheme(com.github.windsekirun.naraeimagepicker.module.PickerSet.getSettingItem())
        setContentView(R.layout.activity_picker)

        catchAll { EventBus.getDefault().register(this) }

        com.github.windsekirun.naraeimagepicker.module.SelectedItem.setLimits(com.github.windsekirun.naraeimagepicker.module.PickerSet.getSettingItem().pickLimit)
        mRequestFileViewMode = com.github.windsekirun.naraeimagepicker.module.PickerSet.getSettingItem().viewMode == com.github.windsekirun.naraeimagepicker.item.enumeration.ViewMode.FileView

        RPermission.instance.checkPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) { integer, _ ->
            if (integer == RPermission.PERMISSION_GRANTED) {
                initFragment(if (mRequestFileViewMode) com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.All else com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.Album)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
        }
    }

    private fun initFragment(mode: com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode, map: Map<String, Any> = mapOf()) {
        mLastFragmentMode = mode

        val fragment = when (mode) {
            com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.Album -> com.github.windsekirun.naraeimagepicker.fragment.AlbumFragment()
            com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.Image -> com.github.windsekirun.naraeimagepicker.fragment.ImageFragment()
            com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.All -> com.github.windsekirun.naraeimagepicker.fragment.AllFragment()
        }

        val bundle = Bundle()
        if (map.isNotEmpty()) {
            for (item in map.entries) {
                when (item.value) {
                    is String -> bundle.putString(item.key, item.value as String)
                    is Int -> bundle.putInt(item.key, item.value as Int)
                }
            }
        }

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, mode.toString()).commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.narae_picker, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when {
            menuItem.itemId == android.R.id.home -> onBackPressed()
            menuItem.itemId == R.id.done -> sendTo()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(mLastFragmentMode.name)
        if (fragment is com.github.windsekirun.naraeimagepicker.fragment.ImageFragment) {
            initFragment(com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.Album)
            return
        }

        com.github.windsekirun.naraeimagepicker.module.SelectedItem.clear()
        com.github.windsekirun.naraeimagepicker.module.PickerSet.clearPickerSet()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    @Subscribe
    fun onToolbarChange(event: com.github.windsekirun.naraeimagepicker.event.ToolbarEvent) {
        supportActionBar?.title = event.item
        supportActionBar?.setDisplayHomeAsUpEnabled(event.isUp)
    }

    @Subscribe
    fun onFragmentTransition(event: com.github.windsekirun.naraeimagepicker.event.FragmentTransitionEvent) {
        if (event.isImage) {
            initFragment(com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.Image,
                    mapOf(com.github.windsekirun.naraeimagepicker.Constants.EXTRA_NAME to event.name))
        } else {
            initFragment(com.github.windsekirun.naraeimagepicker.activity.NaraePickerActivity.FragmentMode.Album)
        }
    }

    @Subscribe
    fun onShowDetail(event: com.github.windsekirun.naraeimagepicker.event.DetailEvent) {
        val intent = Intent(this, com.github.windsekirun.naraeimagepicker.activity.ImageDetailsActivity::class.java)
        intent.putExtra(com.github.windsekirun.naraeimagepicker.Constants.EXTRA_DETAIL_IMAGE, event.path)
        startActivity(intent)
    }

    private fun sendTo() {
        val lists = arrayListOf<String>()
        lists.addAll(com.github.windsekirun.naraeimagepicker.module.SelectedItem.getImageList())

        if (lists.isEmpty()) return

        com.github.windsekirun.naraeimagepicker.module.SelectedItem.clear()
        com.github.windsekirun.naraeimagepicker.module.PickerSet.clearPickerSet()

        val intent = Intent()
        intent.putExtra(com.github.windsekirun.naraeimagepicker.Constants.EXTRA_IMAGE_LIST, lists)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        catchAll { EventBus.getDefault().unregister(this) }
    }
}
package com.github.windsekirun.naraeimagepicker.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.Constants.EXTRA_NAME
import com.github.windsekirun.naraeimagepicker.Constants.RC_READ_STORAGE
import com.github.windsekirun.naraeimagepicker.event.DetailEvent
import com.github.windsekirun.naraeimagepicker.event.FragmentTransitionEvent
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.fragment.AllFragment
import com.github.windsekirun.naraeimagepicker.fragment.FileFragment
import com.github.windsekirun.naraeimagepicker.fragment.FolderFragment
import com.github.windsekirun.naraeimagepicker.item.enumeration.ViewMode
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import com.github.windsekirun.naraeimagepicker.utils.applyCustomPickerTheme
import com.github.windsekirun.naraeimagepicker.utils.catchAll
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pyxis.uzuki.live.naraeimagepicker.R

class NaraePickerActivity : AppCompatActivity() {
    private var lastFragmentMode = FragmentMode.Album
    private var requestFileViewMode = false

    private enum class FragmentMode {
        Album, Image, All
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyCustomPickerTheme(PickerSet.getSettingItem())
        setContentView(R.layout.activity_picker)

        catchAll { EventBus.getDefault().register(this) }

        SelectedItem.setLimits(PickerSet.getSettingItem().pickLimit)
        requestFileViewMode = PickerSet.getSettingItem().viewMode == ViewMode.FileView

        checkPermission()
    }

    private fun checkPermission() {
        val storageReadPermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (storageReadPermission) {
            initFragment(if (requestFileViewMode) FragmentMode.All else FragmentMode.Album)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), RC_READ_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != RC_READ_STORAGE) return

        if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            initFragment(if (requestFileViewMode) FragmentMode.All else FragmentMode.Album)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
    }

    private fun initFragment(mode: FragmentMode, map: Map<String, Any> = mapOf()) {
        lastFragmentMode = mode

        val fragment = when (mode) {
            FragmentMode.Album -> FolderFragment()
            FragmentMode.Image -> FileFragment()
            FragmentMode.All -> AllFragment()
        }

        val bundle = Bundle()
        for (item in map.entries) {
            when (item.value) {
                is String -> bundle.putString(item.key, item.value as String)
                is Int -> bundle.putInt(item.key, item.value as Int)
            }
        }

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, mode.toString()).commitAllowingStateLoss()
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
        val fragment = supportFragmentManager.findFragmentByTag(lastFragmentMode.name)
        if (fragment is FileFragment) {
            initFragment(FragmentMode.Album)
            return
        }

        SelectedItem.clear()
        PickerSet.clearPickerSet()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    @Subscribe
    fun onToolbarChange(event: ToolbarEvent) {
        supportActionBar?.title = event.item
        supportActionBar?.setDisplayHomeAsUpEnabled(event.isUp)
    }

    @Subscribe
    fun onFragmentTransition(event: FragmentTransitionEvent) {
        if (event.isImage) {
            initFragment(FragmentMode.Image, mapOf(EXTRA_NAME to event.name))
        } else {
            initFragment(FragmentMode.Album)
        }
    }

    @Subscribe
    fun onShowDetail(event: DetailEvent) {
        val intent = Intent(this, ImageDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_DETAIL_IMAGE, event.path)
        startActivity(intent)
    }

    private fun sendTo() {
        val lists = arrayListOf<String>()
        lists.addAll(SelectedItem.getImageList())

        if (lists.isEmpty()) return

        SelectedItem.clear()
        PickerSet.clearPickerSet()

        val intent = Intent()
        intent.putExtra(Constants.EXTRA_IMAGE_LIST, lists)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        catchAll { EventBus.getDefault().unregister(this) }
    }
}
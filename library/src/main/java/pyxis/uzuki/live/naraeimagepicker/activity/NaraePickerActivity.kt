package pyxis.uzuki.live.naraeimagepicker.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.DetailEvent
import pyxis.uzuki.live.naraeimagepicker.event.FragmentTransitionEvent
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.fragment.AlbumFragment
import pyxis.uzuki.live.naraeimagepicker.fragment.AllFragment
import pyxis.uzuki.live.naraeimagepicker.fragment.ImageFragment
import pyxis.uzuki.live.naraeimagepicker.module.SelectedItem
import pyxis.uzuki.live.richutilskt.utils.RPermission

class NaraePickerActivity : AppCompatActivity() {
    private var lastFragmentMode = FragmentMode.Album
    private var isRequestAllMode = false

    private enum class FragmentMode {
        Album, Image, All
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        SelectedItem.setLimits(intent.getIntExtra(Constants.EXTRA_LIMIT, Constants.LIMIT_UNLIMITED))
        isRequestAllMode = intent.getBooleanExtra(Constants.EXTRA_REQUEST_ALL_MODE, false)

        EventBus.getDefault().register(this)
        RPermission.instance.checkPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), { integer, _ ->
            if (integer == RPermission.PERMISSION_GRANTED) {
                initFragment(if (isRequestAllMode) FragmentMode.All else FragmentMode.Album)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
        })
    }

    private fun initFragment(mode: FragmentMode, map: Map<String, Any> = mapOf()) {
        lastFragmentMode = mode

        val fragment = when (mode) {
            NaraePickerActivity.FragmentMode.Album -> AlbumFragment()
            NaraePickerActivity.FragmentMode.Image -> ImageFragment()
            NaraePickerActivity.FragmentMode.All -> AllFragment()
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
        val fragment = supportFragmentManager.findFragmentByTag(lastFragmentMode.name)
        if (fragment is ImageFragment) {
            initFragment(FragmentMode.Album)
            return
        }

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
            initFragment(FragmentMode.Image,
                    mapOf(Constants.EXTRA_NAME to event.name))
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

        val intent = Intent()
        intent.putExtra(Constants.EXTRA_IMAGE_LIST, lists)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
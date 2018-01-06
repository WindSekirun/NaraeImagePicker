package pyxis.uzuki.live.naraeimagepicker.activity

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.FragmentTransitionEvent
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.fragment.AlbumFragment
import pyxis.uzuki.live.naraeimagepicker.fragment.ImageFragment
import pyxis.uzuki.live.richutilskt.utils.RPermission

class NaraePickerActivity : AppCompatActivity() {
    private var lastFragmentMode = FragmentMode.Album

    private enum class FragmentMode {
        Album, Image, All
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        RPermission.instance.checkPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), { integer, _ ->
            if (integer == RPermission.PERMISSION_GRANTED) {
                initFragment(FragmentMode.Album)
            }
        })
    }

    private fun initFragment(mode: FragmentMode, map: Map<String, Any> = mapOf()) {
        lastFragmentMode = mode

        val fragment = when (mode) {
            NaraePickerActivity.FragmentMode.Album -> AlbumFragment()
            NaraePickerActivity.FragmentMode.Image -> ImageFragment()
            NaraePickerActivity.FragmentMode.All -> AlbumFragment()
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
        fragmentManager.beginTransaction().replace(R.id.container, fragment, mode.toString()).commitAllowingStateLoss()
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onBackPressed() {
        val fragment = fragmentManager.findFragmentByTag(lastFragmentMode.name)
        if (fragment is ImageFragment) {
            initFragment(FragmentMode.Album)
            return
        }

        super.onBackPressed()
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
}

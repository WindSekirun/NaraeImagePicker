package pyxis.uzuki.live.naraeimagepicker.activity

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.FragmentTransitionEvent
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.folder.AlbumFragment
import pyxis.uzuki.live.naraeimagepicker.folder.ImageFragment
import pyxis.uzuki.live.richutilskt.utils.RPermission

class MainActivity : AppCompatActivity() {

    private enum class FragmentMode {
        Album, Image, All
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        EventBus.getDefault().register(this)
        RPermission.instance.checkPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), { integer, _ ->
            if (integer == RPermission.PERMISSION_GRANTED) {
                initFragment(FragmentMode.Album)
            }
        })
    }

    private fun initFragment(mode: FragmentMode, map: Map<String, Any> = mapOf()) {
        val fragment = when (mode) {
            MainActivity.FragmentMode.Album -> AlbumFragment()
            MainActivity.FragmentMode.Image -> ImageFragment()
            MainActivity.FragmentMode.All -> AlbumFragment()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(mode == FragmentMode.Image)

        val bundle = Bundle()
        if (map.isNotEmpty()) {
            for (item in map.entries) {
                when (item.value) {
                    is String -> bundle.putString(item.key, item.value as String)
                }
            }
        }

        fragment.arguments = bundle
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss()
    }

    @Subscribe
    fun onToolbarChange(event: ToolbarEvent) {
        supportActionBar?.title = event.item
    }

    @Subscribe
    fun onFragmentTransition(event: FragmentTransitionEvent) {
        if (event.isImage) {
            initFragment(FragmentMode.Image,
                    mapOf(Constants.EXTRA_NAME to event.name, Constants.EXTRA_ITEM_COUNT to event.imageCount))
        } else {
            initFragment(FragmentMode.Album)
        }
    }
}

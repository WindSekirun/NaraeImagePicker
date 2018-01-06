package pyxis.uzuki.live.naraeimagepicker.activity

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.event.ToolbarEvent
import pyxis.uzuki.live.naraeimagepicker.folder.AlbumFragment
import pyxis.uzuki.live.richutilskt.utils.RPermission

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        EventBus.getDefault().register(this)
        RPermission.instance.checkPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), { integer, _ ->
            if (integer == RPermission.PERMISSION_GRANTED) {
                initFragment()
            }
        })
    }

    private fun initFragment() {
        val fragment = AlbumFragment()
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss()
    }

    @Subscribe
    fun onToolbarChange(event: ToolbarEvent) {
        supportActionBar?.title = event.item
    }
}

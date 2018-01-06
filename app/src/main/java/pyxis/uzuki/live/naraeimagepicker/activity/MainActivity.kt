package pyxis.uzuki.live.naraeimagepicker.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.folder.AlbumFragment
import pyxis.uzuki.live.naraeimagepicker.impl.OnItemSelectListener

class MainActivity : AppCompatActivity(), OnItemSelectListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)

        val fragment = AlbumFragment()
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss()
    }

    override fun onSelect(index: Int, item: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

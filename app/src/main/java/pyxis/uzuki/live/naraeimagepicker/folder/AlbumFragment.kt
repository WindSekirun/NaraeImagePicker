package pyxis.uzuki.live.naraeimagepicker.folder

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pyxis.uzuki.live.naraeimagepicker.R

/**
 * NaraeImagePicker
 * Class: AlbumFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AlbumFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.fragment_main, container, false) as View
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
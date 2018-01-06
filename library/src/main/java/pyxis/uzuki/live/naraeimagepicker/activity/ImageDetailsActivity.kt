package pyxis.uzuki.live.naraeimagepicker.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_details.*
import pyxis.uzuki.live.naraeimagepicker.Constants

/**
 * NaraeImagePicker
 * Class: ImageDetailsActivity
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val path = intent.getStringExtra(Constants.EXTRA_DETAIL_IMAGE)

        Glide.with(this)
                .load(path)
                .into(photoView)
    }
}
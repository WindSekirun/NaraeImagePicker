package pyxis.uzuki.live.naraeimagepicker.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_details.*
import pyxis.uzuki.live.naraeimagepicker.Constants
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.naraeimagepicker.module.PickerSet
import pyxis.uzuki.live.naraeimagepicker.utils.applyCustomPickerTheme


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
        applyCustomPickerTheme(PickerSet.getSettingItem())
        setContentView(R.layout.activity_image_details)

        val path = intent.getStringExtra(Constants.EXTRA_DETAIL_IMAGE)

        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@ImageDetailsActivity, android.R.color.black)))
            title = ""
            setDisplayHomeAsUpEnabled(true)
            hide()
        }

        photoView.setOnViewTapListener { view, x, y ->
            if (supportActionBar?.isShowing == true) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }

        Glide.with(this)
                .load(path)
                .into(photoView)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when {
            menuItem.itemId == android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}
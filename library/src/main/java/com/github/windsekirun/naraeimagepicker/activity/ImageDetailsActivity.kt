package com.github.windsekirun.naraeimagepicker.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.github.chrisbanes.photoview.PhotoView
import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.utils.loadImage
import pyxis.uzuki.live.naraeimagepicker.R

/**
 * NaraeImagePicker
 * Class: ImageDetailsActivity
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageDetailsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var cardToolbar: CardView
    private lateinit var photoView: PhotoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        val path = intent.getStringExtra(Constants.EXTRA_DETAIL_IMAGE)

        initView()
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        cardToolbar.visibility = View.GONE

        photoView.setOnViewTapListener { _, _, _ ->
            cardToolbar.visibility = if (cardToolbar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        photoView.loadImage(path)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when {
            menuItem.itemId == android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        cardToolbar = findViewById(R.id.cardToolbar)
        photoView = findViewById(R.id.photoView)
    }
}
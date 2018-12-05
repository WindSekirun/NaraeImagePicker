package pyxis.uzuki.live.naraeimagepicker.utils

import android.widget.ImageView
import pyxis.uzuki.live.naraeimagepicker.module.GlideApp

/**
 * NaraeImagePicker
 * Class: GlideEx
 * Created by Pyxis on 2018-12-05.
 *
 * Description:
 */

/**
 * load Image with GlideApp
 * It handle gif, webp extensions as well.
 */
fun ImageView.loadImage(path: String, sizeMultiplier: Float = 0.0f) {
    val gif = path.endsWith(".gif")
    val requestBuilder = if (gif) {
        GlideApp.with(this).asGif().load(path)
    } else {
        GlideApp.with(this).load(path)
    }

    if (sizeMultiplier != 0.0f) {
        requestBuilder.thumbnail(sizeMultiplier).into(this)
    } else {
        requestBuilder.into(this)
    }
}
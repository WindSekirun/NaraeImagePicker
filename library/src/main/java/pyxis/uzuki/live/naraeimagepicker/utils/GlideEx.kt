package pyxis.uzuki.live.naraeimagepicker.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

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
        Glide.with(this).asGif().load(path)
    } else {
        Glide.with(this).load(path)
    }

    if (sizeMultiplier != 0.0f) {
        requestBuilder.thumbnail(sizeMultiplier).into(this)
    } else {
        requestBuilder.into(this)
    }
}
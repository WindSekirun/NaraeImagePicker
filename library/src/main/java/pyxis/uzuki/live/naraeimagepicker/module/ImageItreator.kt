package pyxis.uzuki.live.naraeimagepicker.module

import android.content.Context
import android.provider.MediaStore
import pyxis.uzuki.live.naraeimagepicker.item.AlbumItem
import pyxis.uzuki.live.naraeimagepicker.item.ImageItem
import pyxis.uzuki.live.naraeimagepicker.utils.TimeLogger
import pyxis.uzuki.live.naraeimagepicker.utils.getColumnString
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import pyxis.uzuki.live.richutilskt.utils.toFile

/**
 * NaraeImagePicker
 * Class: ImageItreator
 * Created by Pyxis on 2018-01-12.
 *
 * Description:
 */

object ImageItreator {
    private var mAlbumList = arrayListOf<AlbumItem>()
    private val mTimeLogger = TimeLogger(ImageItreator::class.java.simpleName)

    private val DISPLAY_NAME_COLUMN = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    private val ID_COLUMN = MediaStore.Images.Media._ID
    private val DATA_COLUMN = MediaStore.Images.Media.DATA
    private val MODIFIED_COLUMN = MediaStore.Images.Media.DATE_MODIFIED

    private val mMediaStoreUrl = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val mAlbumProjection = arrayOf(DISPLAY_NAME_COLUMN, DATA_COLUMN)
    private val mImageProjection = arrayOf(ID_COLUMN, DATA_COLUMN, MODIFIED_COLUMN)
    private val mImageSelection = DISPLAY_NAME_COLUMN + " =?"
    private val mOrderByDesc = MODIFIED_COLUMN + " DESC"

    fun start(context: Context, callback: () -> Unit) {
        mTimeLogger.addPart("start")
        runAsync {
            mTimeLogger.addPart("runAsync")
            val results = arrayListOf<AlbumItem>()
            val albumCursor = MediaStore.Images.Media.query(context.contentResolver, mMediaStoreUrl, mAlbumProjection,
                    "", null, mOrderByDesc)
            mTimeLogger.addPart("get albumCursor")
            while (albumCursor.moveToNext()) {
                val name = albumCursor.getColumnString(DISPLAY_NAME_COLUMN)
                val imageList = arrayListOf<ImageItem>()

                val imageCursor = MediaStore.Images.Media.query(context.contentResolver, mMediaStoreUrl, mImageProjection,
                        mImageSelection, arrayOf(name), mOrderByDesc)

//                while (imageCursor.moveToNext()) {
//                    val item = ImageItem.parsingFromCursor(imageCursor)
//                    val file = item.imagePath.toFile()
//                    if (file.exists() && file.canRead()) {
//                        imageList.add(item)
//                    }
//                }
//
//                val albumItem = AlbumItem(name, imageList)
//                albumItem.sortImagesByTimeDesc()
//                results.add(albumItem)
            }
            mTimeLogger.addPart("looping end")
            mTimeLogger.println()
            mAlbumList = results
            runOnUiThread { callback() }
        }
    }

    fun isEmpty() = mAlbumList.isEmpty()

    fun getAlbumList() = mAlbumList
}
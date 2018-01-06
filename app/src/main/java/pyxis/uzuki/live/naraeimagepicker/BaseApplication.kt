package pyxis.uzuki.live.naraeimagepicker

import android.app.Application
import pyxis.uzuki.live.nyancat.NyanCatGlobal
import pyxis.uzuki.live.nyancat.config.TriggerTiming
import pyxis.uzuki.live.nyancat.config.LoggerConfig



/**
 * NaraeImagePicker
 * Class: BaseApplication
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = LoggerConfig(packageName, BuildConfig.DEBUG, TriggerTiming.ALL)
        NyanCatGlobal.breed(config)
    }
}
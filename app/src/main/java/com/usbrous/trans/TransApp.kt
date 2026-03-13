package com.usbrous.trans

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TransApp : Application() {
    override fun onCreate() {
        super.onCreate()
        check(BuildConfig.KEY_PROOF.isNotEmpty() && BuildConfig.KEY_PROOF == BuildConfig.EXPECTED_KEY_PROOF) {
            "[SECURITY] Invalid APP_ACCESS_KEY. Application cannot start."
        }
    }
}

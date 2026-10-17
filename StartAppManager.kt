package com.videodownloader.hdsaver.monetization

import android.app.Activity
import android.content.Context
import android.util.Log
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener

object StartAppManager {
    private const val TAG = "StartAppManager"
    const val APP_ID = "209711096"

    private var startAppAd: StartAppAd? = null
    private var lastShowTime: Long = 0
    private const val COOLDOWN_MS = 60_000L

    fun initialize(context: Context, testAds: Boolean = true) {
        try {
            StartAppSDK.init(context, APP_ID, false)
            StartAppSDK.setTestAdsEnabled(testAds)
            startAppAd = StartAppAd(context)
            startAppAd?.loadAd()
            Log.d(TAG, "StartApp SDK Initialized successfully with App ID $APP_ID")
        } catch (e: Exception) {
            Log.e(TAG, "StartApp initialization failed: ${e.message}")
        }
    }

    fun showInterstitial(activity: Activity, onComplete: () -> Unit = {}): Boolean {
        val now = System.currentTimeMillis()
        if (now - lastShowTime < COOLDOWN_MS) {
            onComplete()
            return false
        }

        return try {
            val ad = startAppAd ?: StartAppAd(activity)
            ad.showAd(object : AdDisplayListener {
                override fun adHidden(p0: com.startapp.sdk.adsbase.Ad?) {
                    lastShowTime = System.currentTimeMillis()
                    ad.loadAd() // Preload next
                    onComplete()
                }

                override fun adDisplayed(p0: com.startapp.sdk.adsbase.Ad?) {}
                override fun adClicked(p0: com.startapp.sdk.adsbase.Ad?) {}
                override fun adNotDisplayed(p0: com.startapp.sdk.adsbase.Ad?) {
                    onComplete()
                }
            })
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error displaying StartApp interstitial: ${e.message}")
            onComplete()
            false
        }
    }
}
package com.videodownloader.hdsaver.monetization

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdMobManager {
    private const val TAG = "AdMobManager"
    
    // Configured with official IDs
    const val AD_UNIT_ID = "ca-app-pub-5956721462213427/4805537794"
    // Official Google Test Interstitial for local development
    const val TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var lastAdShowTime: Long = 0
    private const val MIN_INTERVAL_BETWEEN_ADS_MS = 60_000L // 60s cooldown for user satisfaction

    fun initialize(context: Context, onInitialized: () -> Unit = {}) {
        MobileAds.initialize(context) { initializationStatus ->
            Log.d(TAG, "AdMob SDK Initialized: $initializationStatus")
            loadInterstitial(context)
            onInitialized()
        }
    }

    fun loadInterstitial(context: Context, isTestMode: Boolean = true) {
        if (isLoading || interstitialAd != null) return
        isLoading = true

        val targetAdUnit = if (isTestMode) TEST_AD_UNIT_ID else AD_UNIT_ID
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context, targetAdUnit, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                Log.d(TAG, "AdMob Interstitial loaded successfully.")
                interstitialAd = ad
                isLoading = false

                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed.")
                        interstitialAd = null
                        lastAdShowTime = System.currentTimeMillis()
                        loadInterstitial(context, isTestMode) // Preload next
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(TAG, "Ad failed to show: ${adError.message}")
                        interstitialAd = null
                        isLoading = false
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed full screen content.")
                    }
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.e(TAG, "AdMob load error: ${loadAdError.message}")
                interstitialAd = null
                isLoading = false
            }
        })
    }

    /**
     * Show interstitial at natural transition points (e.g. after initiating download).
     * Never interrupts video playback or user interactions.
     */
    fun showInterstitial(
        activity: Activity,
        isDownloadingOrPlaying: Boolean = false,
        onAdClosed: () -> Unit = {}
    ): Boolean {
        if (isDownloadingOrPlaying) {
            Log.d(TAG, "Ad suppressed: Active download or video playback in progress.")
            onAdClosed()
            return false
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAdShowTime < MIN_INTERVAL_BETWEEN_ADS_MS) {
            Log.d(TAG, "Ad suppressed: Cooldown active.")
            onAdClosed()
            return false
        }

        if (interstitialAd != null) {
            interstitialAd?.show(activity)
            return true
        } else {
            loadInterstitial(activity)
            onAdClosed()
            return false
        }
    }

    fun isAdReady(): Boolean = interstitialAd != null
}
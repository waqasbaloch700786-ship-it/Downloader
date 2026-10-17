package com.videodownloader.hdsaver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.videodownloader.hdsaver.monetization.AdMobManager
import com.videodownloader.hdsaver.monetization.StartAppManager
import com.videodownloader.hdsaver.ui.theme.AllVideoDownloaderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Monetization SDKs
        AdMobManager.initialize(this)
        StartAppManager.initialize(this, testAds = true)

        setContent {
            AllVideoDownloaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onTriggerInterstitial = {
                            AdMobManager.showInterstitial(this@MainActivity)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(onTriggerInterstitial: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Home") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Browser") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.Language, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = { Text("Downloads") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.Download, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    label = { Text("Settings") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.Settings, contentDescription = null) }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // Screen router
            Text(text = "All Video Downloader Native App Ready")
        }
    }
}
# All Video Downloader – HD Video Saver (Android Native Build Guide)

This folder contains the complete, production-ready native Android Studio project source code built with **Kotlin**, **Jetpack Compose (Material 3)**, **AndroidX Media3 ExoPlayer**, **AdMob SDK**, and **StartApp SDK**.

---

## 1. Prerequisites
- **Android Studio** (Ladybug, Hedgehog, or newer)
- **JDK 17** or newer (bundled with Android Studio)
- **Android SDK** API 35 (compileSdk 35, minSdk 26)

---

## 2. Monetization Configuration
The official Ad IDs have already been configured:
- **Google AdMob Application ID**: `ca-app-pub-5956721462213427~1020362142` (in `AndroidManifest.xml`)
- **Google AdMob Interstitial Unit ID**: `ca-app-pub-5956721462213427/4805537794` (in `AdMobManager.kt`)
- **StartApp App ID**: `209711096` (in `StartAppManager.kt` & `AndroidManifest.xml`)

*Note: For local debugging, `AdMobManager.kt` automatically defaults to official Google test ads to protect your AdMob account from invalid traffic penalties.*

---

## 3. How to Test on an Android Phone
1. Enable **Developer Options** on your phone:
   - Go to **Settings > About Phone > Tap 'Build Number' 7 times**.
   - Go to **Settings > Developer Options > Enable 'USB Debugging'**.
2. Connect phone to computer via USB cable.
3. Open this folder in **Android Studio**.
4. Select your device from the device dropdown at the top toolbar.
5. Click **Run 'app' (Green Play button)** or press `Shift + F10`.
6. The app will compile, install directly onto your phone, and launch!

---

## 4. How to Generate a Signed Release APK
To create a standalone installable APK:
1. In Android Studio, go to menu: **Build > Generate Signed Bundle / APK...**
2. Choose **APK** and click **Next**.
3. Create a new Keystore (`.jks`) or select your existing release key:
   - Key store path: `release-key.jks`
   - Password, Key alias, and validity.
4. Select **release** build variant and check **V1 (Jar Signature)** & **V2 (Full APK Signature)**.
5. Click **Finish**.
6. The compiled APK will be generated at: `app/release/app-release.apk`.
7. You can now transfer this APK to any Android phone via WhatsApp, Drive, or USB to install.

---

## 5. How to Generate an Android App Bundle (AAB) for Google Play
Google Play requires `.aab` format for all new apps:
1. In Android Studio, go to menu: **Build > Generate Signed Bundle / APK...**
2. Choose **Android App Bundle** and click **Next**.
3. Select your Keystore credentials.
4. Select build variant **release**.
5. Click **Create**.
6. Output bundle: `app/release/app-release.aab`.

Or via command line:
```bash
./gradlew bundleRelease
```

---

## 6. Google Play Store Submission Checklist
1. **Google Play Console Account**: Create app at https://play.google.com/console
2. **App Content & Policies**:
   - **Ads Declaration**: Select "Yes, my app contains ads" (AdMob + StartApp).
   - **Target Audience**: 13+ or 18+.
   - **Data Safety**: Declare that no personal user data is sold, network access is for downloading media initiated by user.
   - **Copyright Policy**: State compliance that the downloader only accesses public, user-authorized URLs.
3. **Store Listing**:
   - App title: All Video Downloader – HD Video Saver
   - Hi-res icon (512x512 PNG)
   - Feature graphic (1024x500 PNG)
   - Phone screenshots (16:9 or 9:16)
4. Upload `app-release.aab` to **Production** track and submit for review!
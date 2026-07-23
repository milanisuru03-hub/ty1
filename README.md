# YT Player (Android 4.0+ compatible)

A minimal WebView-based YouTube player, targeted at very old Android devices
(`minSdkVersion 14` = Android 4.0 Ice Cream Sandwich).

## What it does
- Opens `m.youtube.com` (the lightweight mobile site) inside a full-screen `WebView`
- Supports browsing/searching/playing videos, including fullscreen video playback
- Hardware back button navigates WebView history before exiting the app

## How to build the APK
This project could not be compiled in the sandbox that generated it (no Android
SDK / network access there), so you'll need to build it yourself — it only
takes a few minutes:

1. Install **Android Studio** (free, from developer.android.com) — or just the
   command-line SDK tools if you prefer.
2. Open this folder (`YouTubePlayer/`) as an existing project in Android Studio.
3. Let Gradle sync (it will download the Android SDK components it needs).
4. Build → Build Bundle(s) / APK(s) → **Build APK(s)**.
5. The generated `app-debug.apk` will be under `app/build/outputs/apk/debug/`.
6. Copy that APK to your Android 4.0 device (via USB, Bluetooth, etc.) and
   install it — you'll need to allow "install from unknown sources" in
   Settings first, since it isn't from the Play Store.

If you don't have Android Studio, you can alternatively build from the command
line with:
```
./gradlew assembleDebug
```
(requires `ANDROID_HOME` pointing to an installed Android SDK).

## Option: build the APK automatically online (no install needed)
This project includes a GitHub Actions workflow
(`.github/workflows/build-apk.yml`) that builds the APK for you in the cloud
— no Android Studio or SDK needed on your own computer:

1. Create a free account at github.com if you don't have one.
2. Create a new repository and upload this whole `YouTubePlayer` folder to it
   (drag-and-drop works on github.com, or use `git push`).
3. Go to the repository's **Actions** tab. The "Build APK" workflow runs
   automatically on push (or click "Run workflow" to trigger it manually).
4. Wait for it to finish (a few minutes) — a green checkmark means success.
5. Open the finished run, scroll to **Artifacts**, and download
   `app-debug-apk` — that's a zip containing your `app-debug.apk`.
6. Transfer that APK to your Android 4.0 device and install it (allow
   "install from unknown sources" first).

## Important limitations to know about
- **Android 4.0 is ~14 years old.** Its WebView is based on a very old WebKit
  engine. YouTube's site has grown much heavier over the years, so pages may
  load slowly, some newer YouTube features may not render, and playback
  performance depends heavily on the specific device's hardware.
- If `m.youtube.com` ever becomes too heavy for the old WebView engine to
  handle, an alternative is to point `YOUTUBE_URL` in `MainActivity.java` at
  individual video **embed** URLs
  (`https://www.youtube.com/embed/VIDEO_ID`), which are much lighter than the
  full site — useful if you mainly want to play specific videos rather than
  browse/search.
- This app only **plays** videos (streaming), it does not download or save
  video files — consistent with YouTube's Terms of Service.
- No API key or Google sign-in is required since it simply loads the public
  mobile website.

## Customizing
- App name: `app/src/main/res/values/strings.xml`
- Icon: `app/src/main/res/mipmap-hdpi/ic_launcher.png` (replace with your own)
- Package name: `applicationId` in `app/build.gradle` and the Java package
  folder structure

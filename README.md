# SickTv for Android TV & Google TV

A clean, customizable, and lightweight home screen launcher and debloating setup for universal Android TV and Google TV devices.

This repository provides the launcher app, mobile companion installer, package management profiles, and setup guides for universal TV devices. Device-specific debloat profiles and package lists are available under [examples/](examples/).

## The home screen

- **Customizable app slots in the dock (3 to 6 apps per row).** Configurable directly from Launcher Settings, with intelligent focus navigation and wrapping. Long-press an app to open a modern liquid-glass flyout menu to move it left/right or open its App Info.
- **Bing Daily Wallpaper Engine with rotation fallback.** Automatically fetches Bing's daily picture with adaptive luminance contrast detection for navigation pills, falling back to bundled Unsplash photos ([docs/wallpaper-sources.md](docs/wallpaper-sources.md)) if offline.
- **Greeting, clock, and weather.** Weather is shown in Celsius using approximate device location and Open-Meteo, with automatic IP-based geolocation fallback.
- **A wide weather bar below the clock.** Displays live temperature and condition on the left, with local city name and source on the right.
- **A combined CPU and memory tile.** One translucent button-style tile shows both usage percentages immediately to the left of Inputs.
- **Liquid-glass Inputs, Wi-Fi, and Settings pills.**
  - **Inputs flyout:** Smooth glass panel showing all active TV input sources.
  - **Settings flyout:** 2-option menu offering quick access to **System Settings** and **Launcher Settings**.
  - **GitHub Update Checker:** Checks GitHub Releases for new updates and highlights the Settings pill and Launcher Settings with a notification dot badge.
- **Launcher Settings:**
  - Choose dock row column count (3, 4, 5, or 6 apps per row).
  - Configurable screensaver idle timer (3 min, 5 min, 10 min, 15 min, or Never).
  - 1-tap screensaver preview and shortcut to TV Screensaver settings.
  - In-app GitHub release update check and download.
- **Standby mode & Ambient Screensaver.** Fixed direct-to-sleep issues with official Android TV `dream_info` metadata, `FLAG_KEEP_SCREEN_ON`, and an in-app idle timeout fallback that displays yesterday's Bing daily photo with bottom-left clock and soft contrast scrim.
- **Remote navigation.** Seamless directional navigation across dock and app rows with focus preservation.

The launcher is the native Android app in the `launcher` folder, with application ID `com.sayan.tvhome`.

## Device debloat and package profiles

Device-specific debloat lists and package records are documented in [examples/](examples/):

- `debloat.txt` lists bloatware packages safe to disable for user 0 across 90+ TV models and streaming devices.
- The debloat workflow uses Android's `pm disable-user` command; it does not uninstall packages, making changes safe and reversible.
- The **SickTv** companion app reads these curated lists and enables 1-tap debloating directly from your mobile phone.

## Install on the TV with SickTv Companion

The companion mobile app **SickTv** (`app.sicktv`) runs directly on your Android phone to discover your TV, debloat it, and manage launchers over wireless ADB without needing a computer or command-line scripts.

### Features of SickTv Companion:
- **Wireless ADB Discovery & Pairing:** Automatically finds Android TVs on your local Wi-Fi network and connects seamlessly.
- **1-Click Curated Debloating:** Safely disables manufacturer bloatware, telemetry, and background services for over 90+ TV models.
- **Launcher Manager & Catalog:**
  - **SickTv Launcher** (`com.sayan.tvhome`) as the primary recommended launcher with direct 1-tap install and updates.
  - Catalog of popular third-party launchers (CleanYourTv, Projectivy Launcher, FLauncher, AT4K Launcher, Monet Launcher).
  - **Restore Stock Home:** 1-tap restore option to safely re-enable the factory default home screen that came with your TV at any time.

### How to use SickTv:
1. Ensure your phone and Android TV are connected to the same Wi-Fi network.
2. Enable **Network Debugging** (or **Wireless Debugging** on Android 11+) in your TV's Developer options.
3. Open **SickTv** on your phone. It automatically scans your local network and discovers your TV.
4. Select your TV, review the debloat list for your model, and tap to disable bloatware.
5. In the launcher tab, select **SickTv Launcher** to automatically install and set it as your default home screen (or choose **Restore Stock Home** if you ever wish to return to the factory launcher).

## Downloads & GitHub Releases

Every push to `main` and release tag automatically builds, signs, and publishes the 2 official APK packages to the repository's [GitHub Releases](https://github.com/Sayan201/SickTv/releases):

- **`launcher.apk`**: SickTv Launcher for Android TV (`com.sayan.tvhome`).
- **`sicktv-companion.apk`**: SickTv Companion app for Android phones (`app.sicktv`).

## Useful files

| Path | Purpose |
|---|---|
| [launcher/](launcher/) | Android TV launcher source and build scripts |
| [launcher/companion/](launcher/companion/) | SickTv mobile companion source and device catalog |
| [tools/](tools/) | ADB connection and package disable/restore utilities |
| [Device profiles](examples/) | Curated package debloat inventories and device notes |
| [Wallpaper credits](docs/wallpaper-sources.md) | Sources and credits for Microsoft Bing Daily Wallpaper Engine and offline photos |
| [config.xml](launcher/app/src/main/res/values/config.xml) | Launcher defaults and settings |

## Privacy and safety

This repository contains no private Wi-Fi credentials, personal account data, or private signing keys. Review the device profile in [examples/](examples/) before disabling packages on your TV.

When setup is finished, turn off Network/ADB debugging in your TV's Developer options and revoke debugging authorizations you no longer need. The launcher itself continues to work normally without ADB.

## Project origin

This project is based on [CleanYourTV by Gathin23](https://github.com/Gathin23/cleanyourtv), which is licensed under MIT. The launcher foundation and its license remain here; this repository expands it into a universal Android TV & Google TV home screen with the SickTv companion installer. See [LICENSE](LICENSE).

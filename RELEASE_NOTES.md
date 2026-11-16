## SickTv v2.3.0 / Companion v1.0.0 (Release 2026-11-16)

### Highlights in This Release

#### SickTv Companion v1.0.0
- **Zangetsu 3D Liquid Glass Interface**:
  - Translucent floating bottom dock with dynamic lens indicator and ambient drop shadows.
  - Fluid pill indicator sliding smoothly beneath active tabs with liquid glass refraction.
  - Frosted translucent card surfaces with crisp, luminous typography and high-contrast readability.
- **Dynamic Liquid Glass Wallpapers**:
  - Interactive wallpaper selector in Companion Settings.
  - Three distinct background aesthetics: **Zangetsu Obsidian** (sleek dark mode), **Cosmic Nebula** (vibrant space glow), and **Deep Aurora** (ethereal celestial emerald/cyan).
- **Unified Apps & Files Navigation**:
  - Merged Apps and Files into a unified workspace with quick-access sub-navigation pills.
  - Streamlined APK installer, file transfer manager, and running/built-in app controls.
- **Dedicated TV Launcher Hub**:
  - Direct access tab to view installed TV launchers and default home screen status.
  - 1-click launcher switching, default home toggling, and accessibility service controls.
  - Built-in catalog of preferred open-source TV launchers (SickTv Launcher, CleanYourTv, Projectivy, FLauncher) with direct install shortcuts.
- **Expanded TV Support (481 Devices)**:
  - Synced debloat and device profiles from CleanYourTv covering 481 Android TV, Google TV, and Fire TV models (Sony, TCL, Xiaomi, Hisense, Philips, Chromecast, Nvidia Shield, Amazon Fire TV, and more).
- **Build & Stability Fixes**:
  - Fixed variable declaration scoping order for apps/files sub-tab state preventing companion release compilation.

#### SickTv Launcher v2.3.0
- **Zangetsu-Style Liquid Glass Dock**:
  - **Ambient Drop Shadow**: Added multi-step soft dark ambient drop shadows underneath (`0x38000000` / `0x22000000` / `0x0E000000`) offset downward, giving the dock realistic physical elevation and separation from wallpapers.
  - **Obsidian Smoked Backplate**: Translucent smoked refraction gradient (`#520A0E18` to `#3805070E`) providing contrast and depth without bluish tinting.
  - **Liquid Frost Dispersion**: Translucent frosted white gradient fill (`0x28FFFFFF` to `0x0EFFFFFF`) with a top Fresnel specular sheen.
  - **Crisp Hairline Rim**: Luminous glass rim stroke with vertical light falloff.
- **Liquid Glass Top Buttons (Inputs, Wi-Fi, Settings, Reorder, Stats)**:
  - Smoked obsidian glass refraction base for physical glass depth.
  - Frosted liquid glass fill with top specular reflection highlights.
  - Glowing high-contrast focused states with luminous white rims.
- **Wi-Fi Glass Flyout Menu**:
  - Clicking the Wi-Fi pill opens an acrylic transparent floating glass dropdown menu.
  - **Connected Network**: Displays active Wi-Fi or Ethernet connection at the top with a green status indicator and "Connected" badge.
  - **Available Networks**: Scans nearby Wi-Fi networks, deduplicates by SSID, sorts by signal strength, and displays lock badges for password-protected networks.
  - **TV-Optimized Scrolling**: Scrollable list capped at TV-safe height for large network lists with smooth remote D-pad navigation.
  - **System Settings Shortcut**: Dedicated "Wi-Fi settings" entry at the bottom to open full system TV Wi-Fi settings.

### Downloads (2 Apps)
- **`launcher.apk`**: SickTv Launcher for Android TV (`com.sayan.tvhome`, v2.3.0).
- **`sicktv-companion.apk`**: SickTv Companion app for Android phones (`app.sicktv`, v1.0.0).

---
*Signed with project release key for persistent sideloading and verified updates.*

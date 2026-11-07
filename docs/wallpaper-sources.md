# Wallpaper sources & credits

SickTv Launcher features a dynamic **Microsoft Bing Daily Wallpaper Engine** for both the home screen and ambient standby screensaver, with offline fallback wallpapers.

## Primary Wallpaper Source: Microsoft Bing

The launcher dynamically queries Microsoft Bing's daily high-resolution photo of the day:
- **Home Screen**: Fetches today's Bing Picture of the Day (`idx=0`) via `https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US`.
- **Standby Mode Screensaver**: Fetches yesterday's Bing photo (`idx=1`) so the screensaver presents a fresh, complementary landscape during idle time.
- **Contrast & Luminance**: Real-time adaptive contrast analysis determines whether the top navigation pills should render dark or light text/icons.
- **Provider**: [Microsoft Bing](https://www.bing.com). All Bing daily images remain the copyright of Microsoft and their respective photographers.

## Offline Fallback Wallpapers (Unsplash)

When the TV is offline or during initial boot before network connectivity is established, SickTv displays bundled high-resolution landscape photos credited below under the [Unsplash License](https://unsplash.com/license):

1. **White Pocket Sunrise** — John Fowler: <https://unsplash.com/photos/a-desert-landscape-with-mountains-in-the-background-tClS6SPq8Zo>
2. **Waves under a moody sky** — Tim Marshall: <https://unsplash.com/photos/landscape-photography-of-ocean-qKlD2QlK-CY>
3. **Tropical forest near mountains** — Aditi Nayar: <https://unsplash.com/photos/landscape-photography-of-forest-xBNpwPOA8iQ>
4. **Mountain lake with a boathouse and surrounding forest** — Rafael Peier: <https://unsplash.com/photos/mountain-lake-with-a-boathouse-and-surrounding-forest-I_XRy-z8m8k>
5. **Beach seashore during sunset** — Cristofer Maximilian: <https://unsplash.com/photos/beach-seashore-during-sunset-uQDRDqpYJHI>
6. **Summer night in the Alps** — Marek Piwnicki: <https://unsplash.com/photos/the-night-sky-over-a-mountain-with-stars-above-it-iy2Y6TBThqI>
7. **Sun over a mountain lake** — Davide Zacchello: <https://unsplash.com/photos/the-sun-shines-brightly-over-a-mountain-lake-xqzHBvjbVL0>

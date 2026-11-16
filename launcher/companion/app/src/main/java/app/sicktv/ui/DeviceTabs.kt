// SPDX-License-Identifier: GPL-3.0-only
// Apps, Files and Tweaks icons are the Material Icons "apps", "folder" and "tune", Upload is
// "upload" and Movie is "movie" (Apache-2.0), see NOTICE.

package app.sicktv.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import app.sicktv.ui.theme.LiquidGlass
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import app.sicktv.ui.theme.GlassDock
import app.sicktv.ui.theme.GlassDockItem
import app.sicktv.ui.theme.GlassCard
import app.sicktv.ui.theme.GlassButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.sicktv.R
import app.sicktv.adb.StatsProfile
import app.sicktv.adb.TvFolder
import app.sicktv.data.Action
import app.sicktv.data.AppInfo
import app.sicktv.data.DeviceState
import app.sicktv.data.Snapshot
import app.sicktv.data.Suggestions
import app.sicktv.data.resolve
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.sqrt

/** The TV page's destinations, one job each, in the bottom bar. */
enum class TvTab(@StringRes val label: Int) {
    Overview(R.string.tabs_overview),
    Apps(R.string.tabs_apps_files),
    Launcher(R.string.tabs_launcher),
    Tweaks(R.string.tabs_tweaks),
}

internal object TabIcons {
    val Apps = icon(
        "Apps",
        "M4 8h4V4H4v4zm6 12h4v-4h-4v4zm-6 0h4v-4H4v4zm0-6h4v-4H4v4zm6 0h4v-4h-4v4zm6-10v4h4V4h-4zm-6 4h4V4h-4v4zm6 6h4v-4h-4v4zm0 6h4v-4h-4v4z",
    )
    val Folder = icon("Folder", "M10 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2z")
    val Launcher = icon(
        "Launcher",
        "M11 21H5c-1.1 0-2-.9-2-2V5c0-1.1.9-2 2-2h6v18zm2 0h6c1.1 0 2-.9 2-2v-7h-8v9zm8-11V5c0-1.1-.9-2-2-2h-6v7h8z",
    )
    val Tune = icon(
        "Tune",
        "M3 17v2h6v-2H3zM3 5v2h10V5H3zm10 16v-2h8v-2h-8v-2h-2v6h2zM7 9v2H3v2h4v2h2V9H7zm14 4v-2H11v2h10zm-6-4h2V7h4V5h-4V3h-2v6z",
    )

    private fun icon(name: String, path: String) = ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f)
        .addPath(PathParser().parsePathString(path).toNodes(), pathFillType = PathFillType.NonZero, fill = SolidColor(Color.Black))
        .build()
}

private fun TvTab.icon() = when (this) {
    TvTab.Overview -> Icons.Default.Home
    TvTab.Apps -> TabIcons.Apps
    TvTab.Launcher -> TabIcons.Launcher
    TvTab.Tweaks -> TabIcons.Tune
}

/**
 * The TV page as four tabs under a bottom bar, in place of one long scroll of folded sections.
 * Every tab leads with the TV itself, big and centred, Overview with a chip per TV to switch; scrolling
 * folds it into the app bar, with the overflow on the right. On Apps, "Search apps" pins under the
 * three buttons; opening it takes the top of the screen and hides the TV until it closes.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class, ExperimentalFoundationApi::class)
@Composable
fun DeviceTabsScreen(
    state: UiState,
    actions: DeviceActions,
    snackbar: SnackbarHostState = remember { SnackbarHostState() },
    startTab: TvTab = TvTab.Overview,
    /** The tab shown, so a screen opened from here can come back to it. */
    onTab: (TvTab) -> Unit = {},
) {
    val device = state.selected ?: return
    val ui = state.device
    val fire = fireInstall(device, ui.apps)
    var tab by rememberSaveable { mutableStateOf(startTab) }
    LaunchedEffect(tab) { onTab(tab) }
    var searching by rememberSaveable { mutableStateOf(false) }
    var appsTab by rememberSaveable { mutableStateOf(AppsTab.Running) }
    var appsFilesSubTab by rememberSaveable { mutableIntStateOf(0) }
    val q = state.query.trim().lowercase()
    val offline = device.state != DeviceState.Online
    // Off Wi-Fi the phone is what's cut off, so the empty tabs say so rather than blame the TV.
    val offWifi = offline && state.offWifi
    val awayIcon = if (offWifi) DeviceIcons.WifiOff else DeviceIcons.Tv
    val awayTitle = (if (offWifi) Copy.PHONE_OFF_WIFI else Copy.TV_OFFLINE).resolve()
    val list = rememberLazyListState()
    val closeSearch = { searching = false; actions.query("") }
    BackHandler(enabled = searching, onBack = closeSearch)
    // The hero tops every tab and collapses into the bar as the tab scrolls.
    var heroHeight by remember { mutableIntStateOf(0) }
    val collapse by remember { derivedStateOf { collapsedFraction(list.firstVisibleItemIndex, list.firstVisibleItemScrollOffset, heroHeight) } }
    val morph = remember { HeroMorph() }
    // A tab shorter than the screen gets blank space at its end, so its hero can still fold fully into the bar.
    // Measured only while the hero and the space are both in view, and kept while scrolled; a different list
    // (another tab, sub-tab or folder, or items coming and going) starts again from none.
    var tail by remember { mutableIntStateOf(0) }
    var measuredFor by remember { mutableStateOf<List<Any>>(emptyList()) }
    val folder by rememberUpdatedState(ui.files.folder)
    LaunchedEffect(list) {
        snapshotFlow { list.layoutInfo }.collect { info ->
            val content = listOf(tab, appsFilesSubTab, appsTab, folder, searching, info.totalItemsCount)
            if (content != measuredFor) { measuredFor = content; tail = 0 }
            val items = info.visibleItemsInfo
            val hero = items.firstOrNull { it.key == "hero" } ?: return@collect
            val end = items.firstOrNull { it.key == "tail" } ?: return@collect
            tail = tailSpace(hero.size, info.mainAxisItemSpacing, end.offset - hero.offset, info.afterContentPadding, info.viewportSize.height)
        }
    }
    // The tabs share one list, so each opens at the top with its hero, not where the last one was left.
    var shown by rememberSaveable { mutableStateOf(tab) }
    var gate by rememberSaveable { mutableStateOf(false) }
    if (gate) {
        val toApps = { gate = false; tab = TvTab.Apps }
        DebloatGate(
            // Only where the picker can install one: the Play Store is on and no launcher was added yet.
            launcher = offerChoose(ui.apps, fire),
            onChoose = { gate = false; actions.installLauncher() },
            onApps = toApps,
            onNeverAgain = { actions.debloatGateOff(); toApps() },
            onDismiss = { gate = false },
        )
    }
    // Each tab refreshes what it shows at its own rate: Overview and Apps (Running, sorted by RAM) need
    // memory per app only, Launcher and Tweaks no processes at all.
    DisposableEffect(tab, device.id) {
        actions.showStats(
            when (tab) {
                TvTab.Overview -> StatsProfile.OVERVIEW
                TvTab.Apps -> StatsProfile.APPS
                TvTab.Launcher, TvTab.Tweaks -> StatsProfile.OTHER
            },
        )
        onDispose { actions.showStats(StatsProfile.OTHER) }
    }
    LaunchedEffect(tab) {
        if (tab == shown) return@LaunchedEffect
        shown = tab
        list.scrollToItem(0)
    }
    // Opening search starts the results from the top; closing it brings the TV back into view.
    var wasSearching by rememberSaveable { mutableStateOf(searching) }
    LaunchedEffect(searching) {
        if (searching == wasSearching) return@LaunchedEffect
        wasSearching = searching
        list.scrollToItem(0)
    }

    var prevTab by remember { mutableStateOf(tab) }
    var tabTransitionProgress by remember { mutableFloatStateOf(1f) }
    LaunchedEffect(tab) {
        if (tab != prevTab) {
            prevTab = tab
            tabTransitionProgress = 0f
            androidx.compose.animation.core.animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = 0.85f,
                    stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow,
                ),
            ) { value, _ ->
                tabTransitionProgress = value
            }
        }
    }

    val wallpaperScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = 1.05f + (0.04f * (1f - tabTransitionProgress)),
        animationSpec = androidx.compose.animation.core.spring(dampingRatio = 0.85f, stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow),
        label = "wallpaperScale",
    )

    Box(Modifier.fillMaxSize()) {
        // Dynamic companion wallpaper background with thick optical glass morphing
        val wallpaperRes = when (state.wallpaper) {
            "nebula" -> R.drawable.bg_wallpaper_nebula
            "aurora" -> R.drawable.bg_wallpaper_aurora
            else -> R.drawable.bg_wallpaper
        }
        Image(
            painter = painterResource(wallpaperRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = wallpaperScale
                    scaleY = wallpaperScale
                },
            contentScale = ContentScale.Crop,
        )
        // Luminous crystal glass scrim: gives deep OLED contrast while letting crystal surfaces refract underlying wallpaper
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0x6606060C),
                            Color(0x2806060C),
                            Color(0x4806060C),
                        ),
                    ),
                ),
        )
        Scaffold(
            containerColor = Color.Transparent,
            // Lifted so a snackbar message isn't hidden behind the floating dock.
            snackbarHost = { SnackbarHost(snackbar, Modifier.padding(bottom = 76.dp)) },
            // Install on Apps and upload on Files as crisp white GlassFab; Launcher has install shortcut
            floatingActionButton = {
                if (!offline) when (tab) {
                    TvTab.Apps -> {
                        if (appsFilesSubTab == 0) {
                            app.sicktv.ui.theme.GlassFab(onClick = actions.pickApp, icon = Icons.Default.Add, text = stringResource(R.string.tabs_install_app))
                        } else {
                            app.sicktv.ui.theme.GlassFab(onClick = { actions.pickFile(ui.files.folder) }, icon = TileIcons.Upload, text = stringResource(R.string.tabs_upload_files))
                        }
                    }
                    TvTab.Launcher -> {
                        app.sicktv.ui.theme.GlassFab(onClick = actions.installLauncher, icon = Icons.Default.Add, text = stringResource(R.string.launcher_install_preferred))
                    }
                    else -> {}
                }
            },
            topBar = {
                if (searching) {
                    SearchHeader(state.query, actions.query, onClose = closeSearch)
                } else {
                    TopAppBar(
                        // Each tab shows the TV big below, and in the bar once it has scrolled under it.
                        title = { TvBarTitle(device, collapse = { collapse }, morph = morph) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.White,
                            actionIconContentColor = Color(0xCCFFFFFF),
                        ),
                        actions = { AppMenu(state, actions) },
                    )
                }
            },
            bottomBar = {
                // The floating Zangetsu dock capsule (Overview, Apps, Launcher, Tweaks)
                // paired with the standalone floating 3D Ruby Remote Orb on the right.
                val targetTabIdx = when (tab) {
                    TvTab.Overview -> 0
                    TvTab.Apps -> 1
                    TvTab.Launcher -> 2
                    TvTab.Tweaks -> 3
                }
                val animatedTabIdx by androidx.compose.animation.core.animateFloatAsState(
                    targetValue = targetTabIdx.toFloat(),
                    animationSpec = androidx.compose.animation.core.spring(
                        dampingRatio = 0.72f,
                        stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow,
                    ),
                    label = "tabGlide",
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GlassDock(
                        shape = CircleShape,
                        indicatorIndex = animatedTabIdx,
                        targetIndex = targetTabIdx.toFloat(),
                        itemCount = 4,
                        modifier = Modifier.weight(1f),
                    ) {
                        TvTab.entries.forEach { t ->
                            GlassDockItem(
                                selected = tab == t,
                                onClick = {
                                    // Leaving Apps closes the search, so the list isn't still filtered on return.
                                    if (t != TvTab.Apps && searching) closeSearch()
                                    tab = t
                                },
                                icon = { Icon(t.icon(), contentDescription = null, Modifier.size(22.dp)) },
                                label = stringResource(t.label),
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    RemoteButton(
                        enabled = !offline,
                        onClick = actions.remote,
                        modifier = Modifier.testTag("barRemote"),
                    )
                }
            },
        ) { pad ->
            // Worked out again only when what they're made from changes, not on every redraw of the page.
            val procs = ui.snapshot?.procs.orEmpty()
            val debloat = remember(ui.history, ui.apps, ui.lists) { debloatState(ui) }
            val found = if (tab == TvTab.Apps && searching) {
                remember(ui.apps, q) {
                    ui.apps.filter { q.isEmpty() || it.label.lowercase().contains(q) || it.packageName.contains(q) }
                        .sortedWith(compareBy({ !it.enabled }, { it.system }, { it.label }))
                }
            } else {
                emptyList()
            }
            val split = if (tab == TvTab.Apps && !searching) {
                remember(ui.apps, procs, appsTab) { splitApps(ui.apps, procs, appsTab) }
            } else {
                null
            }
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(top = pad.calculateTopPadding())
                    .graphicsLayer {
                        alpha = tabTransitionProgress
                        translationY = (1f - tabTransitionProgress) * 16.dp.toPx()
                    }
                    // Sideways swipes step through Running, Installed and Built-in, as the buttons do.
                    .then(if (tab == TvTab.Apps && appsFilesSubTab == 0 && !searching) Modifier.swipeTabs(appsTab.ordinal, AppsTab.entries.size) { appsTab = AppsTab.entries[it] } else Modifier),
                state = list,
                // Content scrolls under the floating liquid glass dock, with bottom padding clearing the dock at rest
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = pad.calculateBottomPadding() + 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                val snap = ui.snapshot
                val live = snap?.takeIf { !offline }
                if (!searching) item(key = "hero") { Box(Modifier.padding(top = 4.dp)) { TvHero(state, live, actions, chips = tab == TvTab.Overview, collapse = { collapse }, morph = morph, onHeight = { heroHeight = it }) } }
                when (tab) {
                    TvTab.Overview -> {
                        val restart = state.restarting[device.id]
                        if (restart != null) {
                            item { RestartingCard(restart, state.clock()) }
                        } else if (device.state == DeviceState.NotAllowed) {
                            item { NotAllowedCard(actions.reconnect) }
                        } else if (offline) {
                            item { OfflineCard(device, state.clock(), offWifi, if (offWifi) actions.openWifi else actions.reconnect) }
                        }
                        // A switch that left the launcher's service off: Home may go back to the TV's own.
                        ui.apps.firstOrNull { it.homeServiceNote }?.takeIf { !offline }?.let { a ->
                            item(key = "serviceOff") { ServiceOffCard(a, onTurnOn = { actions.homeServiceOn(a.packageName) }, onDismiss = { actions.dismissServiceNote(a.packageName) }) }
                        }
                        // Another computer connected needs acting on now, so it sits with the cards above the stats.
                        ui.access?.takeIf { it.warning && !offline }?.let { a -> item(key = "accessWarning") { AccessWarning(a, actions.access, onWho = actions.whoHasAccess) } }
                        // Away, storage is the last one seen, this session's or the saved one after a restart.
                        val used = snap?.storageUsedGb ?: device.lastKnown?.storageUsedGb
                        val total = snap?.storageTotalGb ?: device.lastKnown?.storageTotalGb
                        if (live != null) {
                            item { SystemWidget(live) }
                        } else if (offline && used != null && total != null) {
                            item { SystemWidget(used, total, null, asOf = device.lastSeen?.let { Copy.ago(it, state.clock()).resolve() } ?: stringResource(R.string.tabs_last_visit)) }
                        } else if (offline) {
                            item { Muted(Copy.noStats(device.state).resolve()) }
                        }
                        if (!offline) {
                            item {
                                ActionTiles(
                                    actions,
                                    count = debloat.count,
                                    todo = debloat.todo,
                                    disabling = ui.busy == DISABLING,
                                    // With nothing left on the list, Debloat leads to the apps for one-off changes,
                                    // through a gate that says so until the user turns it off.
                                    onDebloat = when {
                                        debloat.todo -> actions.debloat
                                        state.debloatGate && ui.lists is Suggestions.Ready -> ({ gate = true })
                                        else -> ({ tab = TvTab.Apps })
                                    },
                                )
                            }
                            // A better home screen is a suggestion, so it sits with the actions rather than among the apps.
                            if (offerLauncherCard(ui, debloat, fire)) item(key = "launcherCard") {
                                LauncherCard(ui.apps.firstOrNull { it.home }?.label, onNotNow = actions.hideLauncherCard, onChoose = actions.installLauncher, fire = fire)
                            }
                        }
                        // The list, what's plugged in, and Android.
                        item { SuggestedCard(ui.lists, debloat, offline, onChanges = actions.history) }
                        ui.peripherals?.takeIf { !offline }?.let { p -> item(key = "connectedDevices") { ConnectedDevicesCard(p) } }
                        item { AndroidCard(state, snap, offline, onUpdates = actions.checkUpdates) }
                    }
                    TvTab.Apps -> {
                        if (!searching) {
                            stickyHeader(key = "appsFilesSegment") {
                                PinnedSegments(
                                    listOf(stringResource(R.string.tabs_apps), stringResource(R.string.tabs_files)),
                                    appsFilesSubTab,
                                    lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceContainer, collapse),
                                    inset = 16.dp,
                                ) { appsFilesSubTab = it }
                            }
                        }
                        if (appsFilesSubTab == 0) {
                            fun rows(apps: List<AppInfo>) = items(apps, key = { it.packageName }) { app ->
                                AppRow(app, procs, ui.pending, ui.busy == app.packageName, actions, locked = ui.busy != null, toHomeScreen = { if (searching) closeSearch(); tab = TvTab.Launcher }, offline = offline, onScreen = ui.snapshot?.onScreen)
                            }
                            // Until the first read lands, an empty list means "not yet", not "none".
                            val loading = ui.appsLoading && !offline
                            if (searching) {
                                // One flat list of every matching app, whichever button it sits under; each row says
                                // whether it's running, off or protected.
                                if (found.isEmpty()) item { if (loading) AppsLoading() else EmptyLine(q) }
                                rows(found)
                            } else {
                                tvStore(device, ui.apps)?.let { store -> item(key = "store") { StoreCard(store, offline, locked = ui.busy != null, actions) } }
                                item(key = "appsSubTabs") {
                                    PinnedSegments(
                                        AppsTab.entries.map { stringResource(it.label) }, appsTab.ordinal,
                                        Color.Transparent, inset = 0.dp,
                                        below = { SearchPill(enabled = !offline) { searching = true } },
                                    ) { appsTab = AppsTab.entries[it] }
                                }
                                if (split != null) {
                                    if (split.apps.isEmpty() && split.system.isEmpty()) item {
                                        // Away, the TV can't say what it has, rather than it has nothing.
                                        when {
                                            loading -> AppsLoading()
                                            offline || (appsTab == AppsTab.Running && live == null) -> EmptyState(
                                                awayIcon, awayTitle,
                                                when {
                                                    offWifi -> Copy.OFF_WIFI_LINE
                                                    appsTab == AppsTab.Running -> Copy.RUNNING_OFFLINE
                                                    else -> Copy.APPS_OFFLINE
                                                }.resolve(),
                                            )
                                            else -> EmptyLine(q)
                                        }
                                    }
                                    rows(split.apps)
                                    if (split.system.isNotEmpty()) {
                                        val folded = appsTab in state.foldedSystem
                                        item(key = "system") { SystemHeader(split.system.size, folded) { actions.foldSystem(appsTab, it) } }
                                        if (!folded) rows(split.system)
                                    }
                                }
                            }
                        } else {
                            item(key = "folders") {
                                PinnedSegments(
                                    TvFolder.entries.map { Copy.folder(it).resolve() },
                                    ui.files.folder.ordinal,
                                    Color.Transparent,
                                    inset = 0.dp,
                                ) {
                                    if (!offline) actions.folder(TvFolder.entries[it])
                                }
                            }
                            val folder = ui.files.folder
                            when {
                                offline -> item { EmptyState(awayIcon, awayTitle, if (offWifi) Copy.OFF_WIFI_LINE.resolve() else stringResource(R.string.tabs_files_offline)) }
                                ui.files.error == null && ui.files.list?.isEmpty() == true -> item {
                                    EmptyState(
                                        if (folder == TvFolder.MOVIES) TileIcons.Movie else TabIcons.Folder,
                                        stringResource(R.string.tabs_folder_empty, Copy.folder(folder).resolve()),
                                        stringResource(R.string.tabs_folder_empty_line),
                                    )
                                }
                                else -> item { FilesBlock(ui.files, state.clock(), actions) }
                            }
                        }
                    }
                    TvTab.Launcher -> {
                        if (offline) {
                            item { EmptyState(awayIcon, awayTitle, if (offWifi) Copy.OFF_WIFI_LINE.resolve() else stringResource(R.string.tabs_tweaks_offline)) }
                        } else {
                            item(key = "launcherHeader") {
                                LauncherHubHeader()
                            }
                            item(key = "installedLaunchers") {
                                Panel(stringResource(R.string.launcher_installed_section)) {
                                    HomeScreenBlock(ui, actions, fire)
                                }
                            }
                            item(key = "preferredLauncher") {
                                PreferredLauncherCard(onInstall = actions.installLauncher)
                            }
                        }
                    }
                    TvTab.Tweaks -> {
                        if (offline) item { EmptyState(awayIcon, awayTitle, if (offWifi) Copy.OFF_WIFI_LINE.resolve() else stringResource(R.string.tabs_tweaks_offline)) }
                        else {
                            item { Panel(Copy.TWEAKS.resolve()) { TweaksBlock(ui, actions) } }
                        }
                    }
                }
                item(key = "tail") { Spacer(Modifier.height(with(LocalDensity.current) { tail.toDp() }).testTag("tail")) }
            }
            if (!offline) ui.request?.let { RequestDialog(it, ui.busy == ANSWERING, actions.answerRequest, actions.dismissRequest, actions.closeAlert) }
            ui.sideload?.let { SideloadDialog(it, actions) }
            ui.transfer?.let { TransferDialog(it, actions.cancelTransfer) }
        }
        // Over everything, so the cookie and name can pass from the list into the bar.
        if (!searching) HeroMorphOverlay(device, morph) { collapse }
    }
}

/** A titled card for a tab's block, not folded: each tab has room for it. */
@Composable
private fun Panel(title: String, body: @Composable () -> Unit) {
    GlassCard(Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 4.dp))
            body()
        }
    }
}

@Composable
private fun Muted(text: String) {
    Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun LauncherHubHeader() {
    GlassCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFE50914), Color(0xFF6B040A)),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    TabIcons.Launcher,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp),
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.launcher_menu_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    stringResource(R.string.launcher_menu_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun PreferredLauncherCard(onInstall: () -> Unit) {
    GlassCard(Modifier.fillMaxWidth()) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = LiquidGlass.NeonCrimson,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    stringResource(R.string.launcher_preferred_card_title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            Text(
                stringResource(R.string.launcher_preferred_card_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            GlassButton(
                onClick = onInstall,
                primary = true,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            ) {
                Icon(
                    TabIcons.Launcher,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.launcher_browse_button))
            }
        }
    }
}

/**
 * The overflow, as a bottom sheet: the TV on top, so it's clear which TV its items touch, then this
 * TV's items, the app-wide ones, and Forget on its own. ⋮ carries a dot: red while others are
 * connected, else the primary colour for a change since History was last opened.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppMenu(state: UiState, actions: DeviceActions) {
    var open by remember { mutableStateOf(false) }
    var about by remember { mutableStateOf(false) }
    var forgetting by remember { mutableStateOf(false) }
    var renaming by remember { mutableStateOf(false) }
    val device = state.selected ?: return
    if (about) AboutDialog(
        when (val l = state.device.lists) {
            is Suggestions.NoList -> l.commit
            is Suggestions.Ready -> l.commit
            else -> null
        },
        onDismiss = { about = false },
    )
    if (forgetting) ForgetDialog(name(device), onForget = { forgetting = false; actions.forget(device.id) }, onDismiss = { forgetting = false })
    if (renaming) RenameDialog(device.nickname.orEmpty(), device.model ?: networkName(device), onSave = { renaming = false; actions.rename(device.id, it) }, onDismiss = { renaming = false })
    val c = MaterialTheme.colorScheme
    val context = LocalContext.current
    val others = state.device.access?.others.orEmpty().size
    val fresh = state.historyNew
    IconButton(onClick = { open = true }) {
        // The access warning shows on ⋮ too, so it's there from every tab; it outranks a new change.
        BadgedBox(badge = { MenuDot(others > 0, fresh) }) { Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.tabs_more_options)) }
    }
    if (!open) return
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    // The sheet slides away before what was picked opens.
    val pick = { then: () -> Unit -> scope.launch { sheet.hide() }.invokeOnCompletion { open = false; then() } }
    ModalBottomSheet(
        onDismissRequest = { open = false },
        sheetState = sheet,
        containerColor = Color(0xFF121216),
        dragHandle = {
            Box(
                Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF)),
            )
        },
    ) {
        Column(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp)) {
            Row(Modifier.padding(start = 4.dp, bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Cookie(device, 48.dp)
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(name(device), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    val health = state.device.snapshot?.takeIf { device.state == DeviceState.Online }?.let { Copy.health(it, heaviest(it, state.device.apps)).text.resolve() }
                    val line = listOf(makerLine(device), health.orEmpty()).filter { it.isNotEmpty() }.joinToString(" · ")
                    if (line.isNotEmpty()) Text(line, style = MaterialTheme.typography.bodyMedium, color = Color(0x8AFFFFFF))
                }
            }
            val off = state.device.restore.count
            SheetGroup(
                { MenuRow(Icons.Default.Edit, stringResource(R.string.tabs_rename)) { pick { renaming = true } } },
                { MenuRow(HistoryIcons.History, Copy.HISTORY.resolve(), Copy.toPutBack(off).resolve().takeIf { off > 0 }, dot = { if (fresh) MenuDot(false, true) }) { pick(actions.history) } },
                {
                    MenuRow(
                        AccessIcons.Shield, Copy.WHO_HAS_ACCESS.resolve(),
                        Copy.othersConnected(others).resolve().takeIf { others > 0 }, subColor = LiquidGlass.NeonCrimson,
                        dot = { if (state.device.access?.warning == true) MenuDot(true, false) },
                    ) { pick(actions.whoHasAccess) }
                },
                { MenuRow(Icons.Default.List, Copy.ADVANCED_MONITOR.resolve()) { pick(actions.advanced) } },
            )
            Text("SickTv", style = MaterialTheme.typography.labelLarge, color = Color(0x8AFFFFFF), modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp))
            SheetGroup(
                { MenuRow(Icons.Default.Settings, stringResource(R.string.tabs_settings)) { pick(actions.settings) } },
                { MenuRow(Icons.Default.Share, Copy.SHARE.resolve()) { pick { shareSickTv(context) } } },
                { MenuRow(Icons.Default.Info, stringResource(R.string.tabs_about)) { pick { about = true } } },
            )
            Spacer(Modifier.height(16.dp))
            // Apart, and Forget in red, so neither is one slip from an everyday item.
            SheetGroup(
                { MenuRow(HistoryIcons.History, Copy.RESET_TV.resolve(), Copy.RESET_KEEPS.resolve()) { pick(actions.reset) } },
                { MenuRow(Icons.Default.Delete, stringResource(R.string.tabs_forget), tint = LiquidGlass.NeonCrimson) { pick { forgetting = true } } },
            )
        }
    }
}

/** ⋮'s dot: red for [warning], else the primary colour for something [fresh]; nothing otherwise. */
@Composable
private fun MenuDot(warning: Boolean, fresh: Boolean) {
    if (warning) Badge(containerColor = LiquidGlass.NeonCrimson) else if (fresh) Badge(containerColor = LiquidGlass.NeonCrimson)
}

/** One group of the sheet: rows 2 dp apart, rounded at the group's ends. */
@Composable
private fun SheetGroup(vararg rows: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        rows.forEachIndexed { i, row ->
            Surface(color = Color(0xFF18181E), shape = groupShape(i, rows.size)) { row() }
        }
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    text: String,
    sub: String? = null,
    subColor: Color = Color(0x8AFFFFFF),
    /** Icon and text in this colour, for Forget; else the usual ones. */
    tint: Color? = null,
    dot: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).heightIn(min = 56.dp).padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BadgedBox(badge = { dot() }) { Icon(icon, contentDescription = null, tint = tint ?: Color(0x8AFFFFFF)) }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text, style = MaterialTheme.typography.bodyLarge, color = tint ?: Color.White)
            sub?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = subColor) }
        }
    }
}

/** One group of the menu, as a slightly darker rounded block inside it. */
@Composable
internal fun MenuBlock(items: @Composable () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = Modifier.padding(horizontal = 8.dp),
    ) { Column { items() } }
}

/** "Search apps" under the Apps buttons, pinned with them; a tap opens the search at the top. */
@Composable
private fun SearchPill(enabled: Boolean, onClick: () -> Unit) {
    val shape = CircleShape
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .background(LiquidGlass.DockBackgroundBrush, shape)
            .border(LiquidGlass.DockHairlineBorder, shape),
        shape = shape,
        color = Color.Transparent,
        contentColor = Color.White,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
            Spacer(Modifier.width(12.dp))
            Text(stringResource(R.string.tabs_search_apps), style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), color = Color(0xD0FFFFFF))
        }
    }
}

/**
 * Search, open: the field on top with ← to close and ✕ to clear, and the results get the rest of
 * the screen. The keyboard comes up with it.
 */
@Composable
private fun SearchHeader(query: String, onQuery: (String) -> Unit, onClose: () -> Unit) {
    val focus = remember { FocusRequester() }
    LaunchedEffect(focus) { focus.requestFocus() }
    Column(Modifier.background(Color.Black).statusBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF141418), CircleShape)
                .border(BorderStroke(1.dp, Color(0x24FFFFFF)), CircleShape)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.tabs_close_search), tint = Color.White) }
                TextField(
                    value = query,
                    onValueChange = onQuery,
                    placeholder = { Text(stringResource(R.string.tabs_search_apps), color = Color(0x80FFFFFF)) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LiquidGlass.NeonCrimson,
                    ),
                    modifier = Modifier.weight(1f).focusRequester(focus),
                )
                if (query.isNotEmpty()) IconButton(onClick = { onQuery("") }) { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.tabs_clear_search), tint = Color.White) }
            }
        }
    }
}

/** Where the suggested list stands on this TV, worked out from the live app list. */
internal data class DebloatState(
    /** Apps on the list that are installed here and SickTv may turn off, on or off. */
    val total: Int,
    /** Of those, the ones still on. */
    val on: Int,
    /** Apps SickTv turned off that are on again, often after a TV update. */
    val back: Int,
    /** Apps on the list still on, plus any that came back: the number on the Debloat tile. */
    val count: Int,
    /** Debloat has something to show: apps to turn off, or that this TV has no list. */
    val todo: Boolean,
    /** Of [total], the ones off right now, however they were turned off. */
    val off: Int = 0,
)

/**
 * Debloat with everything on the list already off. [launcher]: a minimal home screen is the next
 * step, so it leads; otherwise Apps does. Don't show again also goes on to Apps.
 */
@Composable
internal fun DebloatGate(launcher: Boolean, onChoose: () -> Unit, onApps: () -> Unit, onNeverAgain: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Broom, contentDescription = null) },
        title = { Text(Copy.GATE_TITLE.resolve()) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(Copy.GATE_DONE.resolve())
                Text((if (launcher) Copy.GATE_LAUNCHER else Copy.GATE_APPS).resolve())
            }
        },
        // Three actions stack full width; two sit side by side.
        confirmButton = {
            if (launcher) {
                Column(Modifier.fillMaxWidth()) {
                    Button(onClick = onChoose, Modifier.fillMaxWidth()) { Text(Copy.CHOOSE_HOME.resolve()) }
                    TextButton(onClick = onApps, Modifier.fillMaxWidth()) { Text(Copy.GO_TO_APPS.resolve()) }
                    TextButton(onClick = onNeverAgain, Modifier.fillMaxWidth()) { Text(Copy.DONT_SHOW_AGAIN.resolve()) }
                }
            } else {
                Button(onClick = onApps) { Text(Copy.GO_TO_APPS.resolve()) }
            }
        },
        dismissButton = if (launcher) null else ({ TextButton(onClick = onNeverAgain) { Text(Copy.DONT_SHOW_AGAIN.resolve()) } }),
    )
}

internal fun debloatState(ui: DeviceUi): DebloatState {
    // History lives on this phone, so it only knows what this phone turned off.
    val ours = ui.history.filter { it.action == Action.Disable && it.undoneAt == null }.map { it.packageName }.toSet()
    val back = ui.apps.filter { it.enabled && it.packageName in ours }.map { it.packageName }.toSet()
    val ready = ui.lists as? Suggestions.Ready
    val listed = ready?.list?.let { l -> l.groups.flatMap { g -> g.packages.map { it.pkg } }.filter { it !in l.keep }.toSet() }.orEmpty()
    val candidates = ui.apps.filter { it.packageName in listed && (!it.enabled || it.protectedReason == null) }
    val on = candidates.filter { it.enabled }.map { it.packageName }.toSet()
    val count = (on + back).size
    val off = candidates.count { !it.enabled }
    return DebloatState(candidates.size, on.size, back.size, count, count > 0 || ui.lists is Suggestions.NoList, off)
}

/**
 * The home screen card waits until the list has loaded and the TV has been debloated: at least one app on the list is
 * off, even one turned off before SickTv, or nothing on it is left on. Apps someone keeps on don't hold it back. A
 * TV with no list has nothing to debloat, so it gets the card once that's known.
 */
internal fun offerLauncherCard(ui: DeviceUi, debloat: DebloatState, fire: Boolean = false) =
    when (ui.lists) {
        is Suggestions.Ready -> debloat.off > 0 || !debloat.todo
        is Suggestions.NoList -> true
        else -> false
    } && offerLauncher(ui.apps, ui.launcherCardHidden, fire)

/**
 * 3D Ruby Glass Remote Orb floating alongside the Zangetsu dock capsule.
 * Styled after the circular volumetric crimson ruby orb from the Zangetsu design reference.
 */
@Composable
private fun RemoteButton(enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val rubyBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFF4D62),
            Color(0xFFE0001B),
            Color(0xFF88000E),
            Color(0xFF380006),
        ),
        radius = 95f,
    )
    val disabledBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF222228), Color(0xFF141418)),
    )
    val borderStroke = if (enabled) {
        BorderStroke(
            0.75.dp,
            Brush.verticalGradient(
                listOf(Color(0xCCFFFFFF), Color(0x60FF4D57), Color(0x20FF4D57)),
            ),
        )
    } else {
        BorderStroke(0.5.dp, Color(0x1EFFFFFF))
    }

    Box(
        modifier = modifier
            .size(56.dp)
            .then(
                if (enabled) {
                    Modifier.shadow(
                        elevation = 14.dp,
                        shape = CircleShape,
                        spotColor = Color(0x66FF4D57),
                        ambientColor = Color(0x33000000),
                    )
                } else Modifier,
            )
            .clip(CircleShape)
            .background(if (enabled) rubyBrush else disabledBrush, CircleShape)
            .border(borderStroke, CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                onClickLabel = stringResource(R.string.tabs_remote),
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (enabled) {
            // 3D Glass spherical highlight arc (top gloss gleam)
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.52f)
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0x55FFFFFF), Color(0x10FFFFFF), Color.Transparent),
                        ),
                    ),
            )
            // Bottom shadow rim for 3D orb volume and refraction depth
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.40f)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(bottomStartPercent = 100, bottomEndPercent = 100))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0x44000000)),
                        ),
                    ),
            )
        }
        Icon(
            imageVector = TileIcons.Remote,
            contentDescription = stringResource(R.string.tabs_remote),
            modifier = Modifier.size(24.dp),
            tint = if (enabled) Color.White else Color(0x55FFFFFF),
        )
    }
}

/**
 * The list in one line of plain text, with See changes beside it. When apps SickTv turned off
 * are on again, one red line says so; the Debloat tile above carries the count and the fix.
 */
@Composable
private fun SuggestedCard(lists: Suggestions?, d: DebloatState, offline: Boolean, onChanges: () -> Unit) {
    GlassCard(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                when (lists) {
                    is Suggestions.Ready -> when {
                        d.total == 0 -> stringResource(R.string.tabs_suggested_none, lists.list.name)
                        d.on == 0 -> pluralStringResource(R.plurals.tabs_suggested_all_off, d.total, d.total)
                        else -> pluralStringResource(R.plurals.tabs_suggested_some_off, d.total, d.total - d.on, d.total)
                    }
                    is Suggestions.NoList -> stringResource(R.string.tabs_suggested_no_list)
                    Suggestions.Unavailable -> stringResource(R.string.tabs_suggested_unavailable)
                    null -> stringResource(if (offline) R.string.tabs_suggested_offline else R.string.tabs_suggested_loading)
                },
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                color = Color.White,
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onChanges)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    stringResource(R.string.tabs_see_changes),
                    color = LiquidGlass.NeonCrimson,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
                )
            }
        }
        if (d.back > 0) {
            val required = stringResource(R.string.tabs_action_required)
            val cameBack = pluralStringResource(R.plurals.tabs_came_back_on, d.back, d.back)
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(required) }
                    append(" ")
                    append(cameBack)
                },
                Modifier.padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
                style = MaterialTheme.typography.bodyLarge, color = LiquidGlass.NeonCrimson,
            )
        }
        val note = when {
            lists is Suggestions.Unavailable -> stringResource(R.string.tabs_note_unavailable)
            lists is Suggestions.NoList -> stringResource(R.string.tabs_note_no_list)
            lists is Suggestions.Ready && lists.byMaker -> stringResource(R.string.tabs_note_by_maker, lists.list.name)
            lists is Suggestions.Ready && !d.todo -> stringResource(R.string.tabs_note_gate)
            else -> null
        }
        note?.let { Text(it, Modifier.padding(end = 8.dp, top = 2.dp), style = MaterialTheme.typography.bodySmall, color = Color(0x8AFFFFFF)) }
        if (lists is Suggestions.Ready) {
            Text(stringResource(R.string.tabs_lists_credit), Modifier.padding(end = 8.dp, top = 2.dp), style = MaterialTheme.typography.bodySmall, color = Color(0x8AFFFFFF))
        }
    }
}

/** Which Android, or Fire OS, and when it last updated, with a way to make the TV look for a newer one. */
@Composable
private fun AndroidCard(state: UiState, snap: Snapshot?, offline: Boolean, onUpdates: () -> Unit) {
    val device = state.selected ?: return
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
    ) {
        StatusRow(
            Icons.Default.Info,
            device.fireOs?.let { stringResource(R.string.tabs_fire_os_version, it) }
                ?: device.android?.let { stringResource(R.string.tabs_android_version, it) } ?: "Android",
            (snap?.softwareMonth ?: device.lastKnown?.softwareMonth)?.let { stringResource(R.string.tabs_updated, Copy.month(it)) },
            stringResource(R.string.tabs_check_updates), !offline, onUpdates,
        )
    }
}

/** The TV's app store, on top of Apps: Open shows it on the TV, and Turn on puts back one that's off. */
@Composable
private fun StoreCard(store: AppInfo, offline: Boolean, locked: Boolean, actions: DeviceActions) {
    val name = stringResource(if (store.packageName == AMAZON_APPSTORE) R.string.tabs_store_amazon else R.string.tabs_store_play)
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
    ) {
        if (store.enabled) {
            StatusRow(TileIcons.Store, name, stringResource(R.string.tabs_store_sub), Copy.button(Action.Open).resolve(), !offline && !locked) {
                actions.openStore(store.packageName, name)
            }
        } else {
            StatusRow(TileIcons.Store, name, stringResource(R.string.tabs_store_off), Copy.button(Action.Enable).resolve(), !offline && !locked) {
                actions.turnOnStore(store.packageName)
            }
        }
    }
}

@Composable
private fun StatusRow(icon: ImageVector, title: String, sub: String?, button: String, enabled: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF22080C)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = LiquidGlass.NeonCrimson)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold), color = Color.White)
            if (sub != null) {
                Spacer(Modifier.height(2.dp))
                Text(sub, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp), color = Color(0x8AFFFFFF))
            }
        }
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(enabled = enabled, onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            Text(
                button,
                color = if (enabled) LiquidGlass.NeonCrimson else Color(0x60FFFFFF),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
            )
        }
    }
}


internal object TileIcons {
    // Absolute commands only: Compose's path parser misplaces a relative move after a close.
    val Upload = icon("Upload", "M9 16H15V10H19L12 3L5 10H9ZM5 18H19V20H5Z")
    // Material's settings_remote: a remote with a ring button and two signal arcs.
    val Remote = icon(
        "Remote",
        "M9 9H15A1 1 0 0 1 16 10V22A1 1 0 0 1 15 23H9A1 1 0 0 1 8 22V10A1 1 0 0 1 9 9Z" +
            "M12 11A2 2 0 1 0 12 15A2 2 0 1 0 12 11Z" +
            "M7.05 6.05L8.46 7.46A5 5 0 0 1 15.54 7.46L16.95 6.05A7 7 0 0 0 7.05 6.05Z" +
            "M4.22 3.22L5.63 4.63A9 9 0 0 1 18.37 4.63L19.78 3.22A11 11 0 0 0 4.22 3.22Z",
    )
    // A storefront after Material's: a scalloped awning over a shop with an open door.
    val Store = icon(
        "Store",
        "M4 4H20L22 9A2.5 2.5 0 0 1 17 9A2.5 2.5 0 0 1 12 9A2.5 2.5 0 0 1 7 9A2.5 2.5 0 0 1 2 9Z" +
            "M4 13H20V20H4Z" +
            "M10 15V20H14V15Z",
    )
    val Movie = icon("Movie", "M18 4l2 4h-3l-2-4h-2l2 4h-3l-2-4H8l2 4H7L5 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V4h-4z")

    private fun icon(name: String, path: String) = ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f)
        .addPath(PathParser().parsePathString(path).toNodes(), pathFillType = PathFillType.NonZero, fill = SolidColor(Color.Black))
        .build()
}

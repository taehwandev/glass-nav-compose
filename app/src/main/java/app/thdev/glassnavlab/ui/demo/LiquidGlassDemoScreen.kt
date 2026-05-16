package app.thdev.glassnavlab.ui.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.thdev.glassnavlab.ui.components.liquidglass.LiquidGlassBackdropHost
import app.thdev.glassnavlab.ui.components.liquidglass.LiquidGlassBottomNavigation
import app.thdev.glassnavlab.ui.components.liquidglass.LiquidGlassNavigationAction
import app.thdev.glassnavlab.ui.components.liquidglass.LiquidGlassNavigationItem
import app.thdev.glassnavlab.ui.components.liquidglass.rememberLiquidGlassNavigationState
import app.thdev.glassnavlab.ui.demo.components.DemoGlassIcon
import app.thdev.glassnavlab.ui.demo.components.DemoHeader
import app.thdev.glassnavlab.ui.demo.components.DemoSampleCard
import app.thdev.glassnavlab.ui.demo.components.DemoSurfaceCard
import app.thdev.glassnavlab.ui.demo.model.DemoBackgroundColor
import app.thdev.glassnavlab.ui.demo.model.DemoDestination
import app.thdev.glassnavlab.ui.demo.model.GlassNavIcon
import app.thdev.glassnavlab.ui.demo.model.backdropPaletteForItem
import app.thdev.glassnavlab.ui.demo.model.destinationFor
import app.thdev.glassnavlab.ui.demo.model.sampleDemoPalette

@Composable
fun LiquidGlassDemoScreen() {
    val items = rememberDemoNavigationItems()
    val navigationState = rememberLiquidGlassNavigationState(
        initialSelectedItemId = items.first().id,
    )
    val selectedDestination = destinationFor(navigationState.selectedItemId)
    val listState = rememberDestinationListState(selectedDestination.id)
    val navigationBackdropColor by rememberNavigationBackdropColor(
        listState = listState,
        destination = selectedDestination,
    )

    LiquidGlassBackdropHost(
        modifier = Modifier
            .fillMaxSize()
            .background(DemoBackgroundColor),
        backgroundColor = DemoBackgroundColor,
        content = {
            DemoContent(
                destination = selectedDestination,
                listState = listState,
            )
        },
        floatingContent = { backdrop ->
            LiquidGlassBottomNavigation(
                items = items,
                state = navigationState,
                backdrop = backdrop,
                modifier = Modifier.align(Alignment.BottomCenter),
                adaptiveBackgroundColor = navigationBackdropColor,
                trailingAction = LiquidGlassNavigationAction(
                    contentDescription = "Create",
                    icon = { color -> DemoGlassIcon(GlassNavIcon.Create, color) },
                    onClick = { navigationState.select("create") },
                ),
            )
        },
    )
}

@Composable
private fun rememberDestinationListState(destinationId: String): LazyListState {
    val homeListState = rememberLazyListState()
    val searchListState = rememberLazyListState()
    val createListState = rememberLazyListState()
    val profileListState = rememberLazyListState()

    return when (destinationId) {
        "search" -> searchListState
        "create" -> createListState
        "profile" -> profileListState
        else -> homeListState
    }
}

@Composable
private fun rememberDemoNavigationItems(): List<LiquidGlassNavigationItem> {
    return remember {
        listOf(
            LiquidGlassNavigationItem(
                id = "home",
                label = "Home",
                icon = { _, color -> DemoGlassIcon(GlassNavIcon.Home, color) },
            ),
            LiquidGlassNavigationItem(
                id = "search",
                label = "Search",
                icon = { _, color -> DemoGlassIcon(GlassNavIcon.Search, color) },
            ),
            LiquidGlassNavigationItem(
                id = "create",
                label = "Create",
                icon = { _, color -> DemoGlassIcon(GlassNavIcon.Create, color) },
            ),
            LiquidGlassNavigationItem(
                id = "profile",
                label = "Profile",
                icon = { _, color -> DemoGlassIcon(GlassNavIcon.Profile, color) },
            ),
        )
    }
}

@Composable
private fun DemoContent(
    destination: DemoDestination,
    listState: LazyListState,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 64.dp,
            end = 20.dp,
            bottom = 150.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(key = "header-${destination.id}") {
            DemoHeader(destination = destination)
        }

        itemsIndexed(
            items = destination.samples,
            key = { index, sample -> "${destination.id}-sample-${sample.title}-$index" },
        ) { _, sample ->
            DemoSampleCard(sample = sample)
        }

        itemsIndexed(
            items = destination.surfaces,
            key = { index, surface -> "${destination.id}-surface-${surface.title}-$index" },
        ) { index, surface ->
            DemoSurfaceCard(
                surface = surface,
                index = index,
            )
        }
    }
}

@Composable
private fun rememberNavigationBackdropColor(
    listState: LazyListState,
    destination: DemoDestination,
): State<Color> {
    val density = LocalDensity.current
    val sampleOffsetFromBottom = with(density) { 88.dp.roundToPx() }

    return remember(listState, destination.id, sampleOffsetFromBottom) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val sampleY = layoutInfo.viewportEndOffset - sampleOffsetFromBottom
            val visibleCard = layoutInfo.visibleItemsInfo.firstOrNull { item ->
                item.index > 0 && sampleY >= item.offset && sampleY <= item.offset + item.size
            } ?: return@derivedStateOf DemoBackgroundColor
            val palette = backdropPaletteForItem(
                destination = destination,
                itemIndex = visibleCard.index,
            ) ?: return@derivedStateOf DemoBackgroundColor
            val localFraction = ((sampleY - visibleCard.offset).toFloat() / visibleCard.size)
                .coerceIn(0f, 1f)

            sampleDemoPalette(
                palette = palette,
                fraction = localFraction,
            )
        }
    }
}

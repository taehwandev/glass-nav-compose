package app.thdev.glassnavlab.feature.feed

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import app.thdev.glassnavlab.core.designsystem.theme.NotmidColorTokens
import app.thdev.glassnavlab.feature.feed.api.ClipDetailRoute
import app.thdev.glassnavlab.feature.feed.api.FeedRouteEvent
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidDestinationContent
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidRouteDetailContent
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidClip
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidDestination
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidPlace

@Composable
fun FeedScreen(
    destination: NotmidDestination,
    listState: LazyListState,
    onRouteEvent: (FeedRouteEvent) -> Unit = {},
) {
    NotmidDestinationContent(
        destination = destination,
        listState = listState,
        onClipSelected = { clip ->
            onRouteEvent(FeedRouteEvent.ClipRequested(clip.id))
        },
    )
}

@Composable
fun ClipDetailScreen(
    destination: NotmidDestination,
    route: ClipDetailRoute,
    listState: LazyListState,
) {
    val clip = remember(destination.id, route.clipId) {
        destination.clips.firstOrNull { it.id == route.clipId }
    }
    val primaryClip = clip ?: NotmidClip(
        id = route.clipId,
        title = "Clip",
        description = "This clip route is valid, but local fake content has no matching item.",
        badge = "missing",
        palette = listOf(NotmidColorTokens.Ink, NotmidColorTokens.Subtle, NotmidColorTokens.Mist),
    )
    val primaryPlace = remember(destination.id, route.clipId) {
        destination.places.firstOrNull() ?: NotmidPlace(
            id = "clip-${route.clipId}-place",
            title = "Linked place",
            description = "A real service would resolve the clip's attached place from data.",
            metric = "clip",
            palette = primaryClip.palette,
            height = 176.dp,
            contentColor = NotmidColorTokens.Cloud,
        )
    }

    NotmidRouteDetailContent(
        routeTitle = primaryClip.title,
        routeSubtitle = primaryClip.description,
        routeMeta = "clips/${route.clipId}",
        primaryClip = primaryClip,
        primaryPlace = primaryPlace,
        listState = listState,
    )
}

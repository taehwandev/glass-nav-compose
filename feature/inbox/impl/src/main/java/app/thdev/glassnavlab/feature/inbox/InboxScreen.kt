package app.thdev.glassnavlab.feature.inbox

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import app.thdev.glassnavlab.core.designsystem.theme.NotmidColorTokens
import app.thdev.glassnavlab.feature.inbox.api.ChatThreadRoute
import app.thdev.glassnavlab.feature.inbox.api.InboxRouteEvent
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidDestinationContent
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidRouteDetailContent
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidClip
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidDestination
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidPlace

@Composable
fun InboxScreen(
    destination: NotmidDestination,
    listState: LazyListState,
    onRouteEvent: (InboxRouteEvent) -> Unit = {},
) {
    NotmidDestinationContent(
        destination = destination,
        listState = listState,
        onClipSelected = { clip ->
            onRouteEvent(InboxRouteEvent.ChatThreadRequested(clip.id))
        },
        onPlaceSelected = { place ->
            onRouteEvent(InboxRouteEvent.ChatThreadRequested(place.id))
        },
    )
}

@Composable
fun ChatThreadScreen(
    destination: NotmidDestination,
    route: ChatThreadRoute,
    listState: LazyListState,
) {
    val relatedClip = remember(destination.id, route.threadId) {
        destination.clips.firstOrNull { it.id == route.threadId }
    }
    val relatedPlace = remember(destination.id, route.threadId) {
        destination.places.firstOrNull { it.id == route.threadId }
    }
    val primaryClip = relatedClip ?: NotmidClip(
        id = route.threadId,
        title = "Chat thread",
        description = "Messages around a shared clip, place, or plan land under this route.",
        badge = "chat",
        palette = relatedPlace?.palette ?: listOf(
            NotmidColorTokens.Ink,
            NotmidColorTokens.RouteBlue,
            NotmidColorTokens.Mist,
        ),
    )
    val primaryPlace = relatedPlace ?: NotmidPlace(
        id = "chat-${route.threadId}-context",
        title = "Conversation context",
        description = "A real service would resolve the shared object and hydrate messages here.",
        metric = "dm",
        palette = primaryClip.palette,
        height = 176.dp,
        contentColor = NotmidColorTokens.Cloud,
    )

    NotmidRouteDetailContent(
        routeTitle = primaryClip.title,
        routeSubtitle = primaryClip.description,
        routeMeta = "chats/${route.threadId}",
        primaryClip = primaryClip,
        primaryPlace = primaryPlace,
        listState = listState,
    )
}

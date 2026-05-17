package app.thdev.glassnavlab.feature.map

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import app.thdev.glassnavlab.core.designsystem.theme.NotmidColorTokens
import app.thdev.glassnavlab.feature.map.api.MapRouteEvent
import app.thdev.glassnavlab.feature.map.api.PlaceDetailRoute
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidDestinationContent
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidRouteDetailContent
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidClip
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidDestination
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidPlace

@Composable
fun MapScreen(
    destination: NotmidDestination,
    listState: LazyListState,
    onRouteEvent: (MapRouteEvent) -> Unit = {},
) {
    NotmidDestinationContent(
        destination = destination,
        listState = listState,
        onPlaceSelected = { place ->
            onRouteEvent(MapRouteEvent.PlaceRequested(place.id))
        },
    )
}

@Composable
fun PlaceDetailScreen(
    destination: NotmidDestination,
    route: PlaceDetailRoute,
    listState: LazyListState,
) {
    val place = remember(destination.id, route.placeId) {
        destination.places.firstOrNull { it.id == route.placeId }
    }
    val primaryPlace = place ?: NotmidPlace(
        id = route.placeId,
        title = "Place",
        description = "This place route is valid, but local fake content has no matching item.",
        metric = "missing",
        palette = listOf(NotmidColorTokens.Ink, NotmidColorTokens.Subtle, NotmidColorTokens.Mist),
        height = 176.dp,
        contentColor = NotmidColorTokens.Cloud,
    )
    val primaryClip = remember(destination.id, route.placeId) {
        destination.clips.firstOrNull() ?: NotmidClip(
            id = "place-${route.placeId}-clip",
            title = "Recent proof",
            description = "A real service would load clips attached to this place.",
            badge = "place",
            palette = primaryPlace.palette,
        )
    }

    NotmidRouteDetailContent(
        routeTitle = primaryPlace.title,
        routeSubtitle = primaryPlace.description,
        routeMeta = "places/${route.placeId}",
        primaryClip = primaryClip,
        primaryPlace = primaryPlace,
        listState = listState,
    )
}

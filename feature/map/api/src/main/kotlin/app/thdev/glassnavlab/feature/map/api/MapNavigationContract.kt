package app.thdev.glassnavlab.feature.map.api

import app.thdev.glassnavlab.core.router.DeepLinkSpec
import app.thdev.glassnavlab.core.router.RouteEvent
import app.thdev.glassnavlab.core.router.RouteStack
import app.thdev.glassnavlab.core.router.WebRouteLink
import app.thdev.glassnavlab.feature.notmid.api.NotmidDestinationIds
import app.thdev.glassnavlab.feature.notmid.api.NotmidRoute
import app.thdev.glassnavlab.feature.notmid.api.NotmidRouteSpec
import app.thdev.glassnavlab.feature.notmid.api.NotmidStaticDeepLinkSpec
import app.thdev.glassnavlab.feature.notmid.api.NotmidTopLevelRouteSpec
import app.thdev.glassnavlab.feature.notmid.api.NotmidTopLevelRoute

object MapRoute : NotmidTopLevelRoute {
    override val route: String = "notmid/map"
    override val selectedDestinationId: String = NotmidDestinationIds.MAP
    override val title: String = "Map"
    override val webPathSegments: List<String> = listOf(NotmidDestinationIds.MAP)
}

object MapRouteSpec : NotmidTopLevelRouteSpec<MapRoute> {
    override val route: MapRoute = MapRoute
}

object MapDeepLinkSpec : NotmidStaticDeepLinkSpec(MapRoute)

data class PlaceDetailRoute(
    val placeId: String,
) : NotmidRoute {
    init {
        require(placeId.isNotBlank()) { "placeId must not be blank." }
    }

    override val route: String = "notmid/places/$placeId"
    override val selectedDestinationId: String = NotmidDestinationIds.MAP
    override val title: String = "Place"
    override val webPathSegments: List<String> = listOf(PLACES_PATH, placeId)
}

object PlaceDetailRouteSpec : NotmidRouteSpec<PlaceDetailRoute> {
    override val routePattern: String = "notmid/places/{placeId}"

    fun create(placeId: String): PlaceDetailRoute {
        return PlaceDetailRoute(placeId)
    }
}

object PlaceDeepLinkSpec : DeepLinkSpec {
    override val priority: Int = 20

    override fun match(link: WebRouteLink): RouteStack? {
        val placeId = when {
            link.pathSegments.size == 2 && link.pathSegments[0] == PLACES_PATH -> {
                link.pathSegments[1]
            }

            link.pathSegments.size == 3 &&
                link.pathSegments[0] == NotmidDestinationIds.MAP &&
                link.pathSegments[1] == PLACES_PATH -> {
                link.pathSegments[2]
            }

            else -> return null
        }.takeIf(String::isNotBlank) ?: return null

        return RouteStack.of(
            MapRoute,
            PlaceDetailRouteSpec.create(placeId),
        )
    }
}

sealed interface MapRouteEvent : RouteEvent {
    data class PlaceRequested(
        val placeId: String,
    ) : MapRouteEvent
}

private const val PLACES_PATH = "places"

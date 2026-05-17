package app.thdev.glassnavlab.feature.feed.api

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

object FeedRoute : NotmidTopLevelRoute {
    override val route: String = "notmid/feed"
    override val selectedDestinationId: String = NotmidDestinationIds.FEED
    override val title: String = "Feed"
    override val webPathSegments: List<String> = listOf(NotmidDestinationIds.FEED)
}

object FeedRouteSpec : NotmidTopLevelRouteSpec<FeedRoute> {
    override val route: FeedRoute = FeedRoute
}

object FeedDeepLinkSpec : NotmidStaticDeepLinkSpec(FeedRoute)

data class ClipDetailRoute(
    val clipId: String,
) : NotmidRoute {
    init {
        require(clipId.isNotBlank()) { "clipId must not be blank." }
    }

    override val route: String = "notmid/clips/$clipId"
    override val selectedDestinationId: String = NotmidDestinationIds.FEED
    override val title: String = "Clip"
    override val webPathSegments: List<String> = listOf(CLIPS_PATH, clipId)
}

object ClipDetailRouteSpec : NotmidRouteSpec<ClipDetailRoute> {
    override val routePattern: String = "notmid/clips/{clipId}"

    fun create(clipId: String): ClipDetailRoute {
        return ClipDetailRoute(clipId)
    }
}

object ClipDeepLinkSpec : DeepLinkSpec {
    override val priority: Int = 20

    override fun match(link: WebRouteLink): RouteStack? {
        val clipId = when {
            link.pathSegments.size == 2 && link.pathSegments[0] == CLIPS_PATH -> {
                link.pathSegments[1]
            }

            link.pathSegments.size == 3 &&
                link.pathSegments[0] == NotmidDestinationIds.FEED &&
                link.pathSegments[1] == CLIPS_PATH -> {
                link.pathSegments[2]
            }

            else -> return null
        }.takeIf(String::isNotBlank) ?: return null

        return RouteStack.of(
            FeedRoute,
            ClipDetailRouteSpec.create(clipId),
        )
    }
}

sealed interface FeedRouteEvent : RouteEvent {
    data class ClipRequested(
        val clipId: String,
    ) : FeedRouteEvent
}

private const val CLIPS_PATH = "clips"

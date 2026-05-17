package app.thdev.glassnavlab.feature.notmid.api

import app.thdev.glassnavlab.core.router.RouteEvent
import app.thdev.glassnavlab.core.router.ComposeRoute
import app.thdev.glassnavlab.core.router.RouteSpec
import app.thdev.glassnavlab.core.router.RouteStack
import app.thdev.glassnavlab.core.router.DeepLinkSpec
import app.thdev.glassnavlab.core.router.TopLevelRouteSpec
import app.thdev.glassnavlab.core.router.WebRoute
import app.thdev.glassnavlab.core.router.WebRouteLink

interface NotmidRoute : ComposeRoute, WebRoute {
    val selectedDestinationId: String
    val title: String
}

interface NotmidTopLevelRoute : NotmidRoute

interface NotmidRouteSpec<out R : NotmidRoute> : RouteSpec<R>

interface NotmidTopLevelRouteSpec<out R : NotmidTopLevelRoute> :
    NotmidRouteSpec<R>,
    TopLevelRouteSpec<R> {
    override val destinationId: String
        get() = route.selectedDestinationId

    override val title: String
        get() = route.title
}

open class NotmidStaticDeepLinkSpec(
    private val route: NotmidRoute,
    private val stackFactory: () -> RouteStack = { RouteStack.single(route) },
    override val priority: Int = 0,
) : DeepLinkSpec {
    override fun match(link: WebRouteLink): RouteStack? {
        if (link.pathSegments != route.webPathSegments) return null
        return stackFactory()
    }
}

object NotmidDestinationIds {
    const val FEED = "feed"
    const val MAP = "map"
    const val CAPTURE = "capture"
    const val INBOX = "inbox"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
}

sealed interface NotmidRouteEvent : RouteEvent {
    data class DestinationSelected(
        val destinationId: String,
    ) : NotmidRouteEvent

    object SettingsRequested : NotmidRouteEvent
}

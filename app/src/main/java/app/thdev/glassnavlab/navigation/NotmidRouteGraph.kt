package app.thdev.glassnavlab.navigation

import app.thdev.glassnavlab.core.router.RouteStack
import app.thdev.glassnavlab.core.router.WebRouteLink
import app.thdev.glassnavlab.core.router.impl.DefaultRouteRegistry
import app.thdev.glassnavlab.feature.capture.api.CaptureDeepLinkSpec
import app.thdev.glassnavlab.feature.capture.api.CaptureRouteSpec
import app.thdev.glassnavlab.feature.feed.api.ClipDeepLinkSpec
import app.thdev.glassnavlab.feature.feed.api.ClipDetailRouteSpec
import app.thdev.glassnavlab.feature.feed.api.FeedDeepLinkSpec
import app.thdev.glassnavlab.feature.feed.api.FeedRoute
import app.thdev.glassnavlab.feature.feed.api.FeedRouteSpec
import app.thdev.glassnavlab.feature.inbox.api.ChatDeepLinkSpec
import app.thdev.glassnavlab.feature.inbox.api.ChatThreadRouteSpec
import app.thdev.glassnavlab.feature.inbox.api.InboxRoute
import app.thdev.glassnavlab.feature.inbox.api.InboxDeepLinkSpec
import app.thdev.glassnavlab.feature.inbox.api.InboxRouteSpec
import app.thdev.glassnavlab.feature.map.api.MapDeepLinkSpec
import app.thdev.glassnavlab.feature.map.api.MapRouteSpec
import app.thdev.glassnavlab.feature.map.api.MapRoute
import app.thdev.glassnavlab.feature.map.api.PlaceDeepLinkSpec
import app.thdev.glassnavlab.feature.map.api.PlaceDetailRouteSpec
import app.thdev.glassnavlab.feature.notmid.api.NotmidRoute
import app.thdev.glassnavlab.feature.profile.api.ProfileDeepLinkSpec
import app.thdev.glassnavlab.feature.profile.api.ProfileRoute
import app.thdev.glassnavlab.feature.profile.api.ProfileRouteSpec
import app.thdev.glassnavlab.feature.profile.api.ProfileSettingsDeepLinkSpec
import app.thdev.glassnavlab.feature.profile.api.ProfileSettingsRoute
import app.thdev.glassnavlab.feature.webview.api.WebViewDeepLinkSpec

internal object NotmidRouteGraph {
    private val registry = DefaultRouteRegistry(
        defaultRoute = FeedRoute,
        topLevelRouteSpecs = listOf(
            FeedRouteSpec,
            MapRouteSpec,
            CaptureRouteSpec,
            InboxRouteSpec,
            ProfileRouteSpec,
        ),
        deepLinkSpecs = listOf(
            ClipDeepLinkSpec,
            PlaceDeepLinkSpec,
            ChatDeepLinkSpec,
            ProfileSettingsDeepLinkSpec,
            FeedDeepLinkSpec,
            MapDeepLinkSpec,
            CaptureDeepLinkSpec,
            InboxDeepLinkSpec,
            ProfileDeepLinkSpec,
            WebViewDeepLinkSpec,
        ),
    )

    val defaultRoute: NotmidRoute = FeedRoute

    fun destination(destinationId: String): NotmidRoute {
        return registry.stackForDestination(destinationId)
            ?.topRoute as? NotmidRoute
            ?: defaultRoute
    }

    fun settingsStack(): RouteStack {
        return RouteStack.of(
            ProfileRoute,
            ProfileSettingsRoute,
        )
    }

    fun clipStack(clipId: String): RouteStack {
        return RouteStack.of(
            FeedRoute,
            ClipDetailRouteSpec.create(clipId),
        )
    }

    fun placeStack(placeId: String): RouteStack {
        return RouteStack.of(
            MapRoute,
            PlaceDetailRouteSpec.create(placeId),
        )
    }

    fun chatThreadStack(threadId: String): RouteStack {
        return RouteStack.of(
            InboxRoute,
            ChatThreadRouteSpec.create(threadId),
        )
    }

    fun resolveWebLink(link: WebRouteLink): RouteStack? {
        return registry.resolve(link)
    }
}

package app.thdev.glassnavlab.feature.profile.api

import app.thdev.glassnavlab.core.router.DeepLinkSpec
import app.thdev.glassnavlab.core.router.RouteStack
import app.thdev.glassnavlab.core.router.WebRouteLink
import app.thdev.glassnavlab.feature.notmid.api.NotmidDestinationIds
import app.thdev.glassnavlab.feature.notmid.api.NotmidRoute
import app.thdev.glassnavlab.feature.notmid.api.NotmidStaticDeepLinkSpec
import app.thdev.glassnavlab.feature.notmid.api.NotmidTopLevelRouteSpec
import app.thdev.glassnavlab.feature.notmid.api.NotmidTopLevelRoute

object ProfileRoute : NotmidTopLevelRoute {
    override val route: String = "notmid/profile"
    override val selectedDestinationId: String = NotmidDestinationIds.PROFILE
    override val title: String = "Profile"
    override val webPathSegments: List<String> = listOf(NotmidDestinationIds.PROFILE)
}

object ProfileRouteSpec : NotmidTopLevelRouteSpec<ProfileRoute> {
    override val route: ProfileRoute = ProfileRoute
}

object ProfileDeepLinkSpec : NotmidStaticDeepLinkSpec(ProfileRoute)

object ProfileSettingsRoute : NotmidRoute {
    override val route: String = "notmid/profile/settings"
    override val selectedDestinationId: String = NotmidDestinationIds.PROFILE
    override val title: String = "Settings"
    override val webPathSegments: List<String> = listOf(NotmidDestinationIds.SETTINGS)
}

object ProfileSettingsDeepLinkSpec : DeepLinkSpec {
    override val priority: Int = 10

    override fun match(link: WebRouteLink): RouteStack? {
        if (link.pathSegments != listOf(NotmidDestinationIds.PROFILE, NotmidDestinationIds.SETTINGS)) {
            return null
        }

        return RouteStack.of(
            ProfileRoute,
            ProfileSettingsRoute,
        )
    }
}

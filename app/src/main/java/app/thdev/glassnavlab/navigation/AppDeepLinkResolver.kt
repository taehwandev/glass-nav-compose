package app.thdev.glassnavlab.navigation

import app.thdev.glassnavlab.core.router.RouteCommand
import app.thdev.glassnavlab.core.router.WebRouteLink

internal class AppDeepLinkResolver {
    fun resolve(uriString: String): RouteCommand? {
        val link = WebRouteLink.parse(uriString) ?: return null
        if (link.scheme != APP_WEB_SCHEME || link.host != APP_WEB_HOST) return null

        val featurePathSegments = link.pathSegmentsAfter(APP_WEB_BASE_PATH) ?: return null
        val stack = NotmidRouteGraph.resolveWebLink(
            link.copy(pathSegments = featurePathSegments),
        ) ?: return null

        return RouteCommand(stack = stack)
    }

    companion object {
        const val APP_WEB_SCHEME = "https"
        const val APP_WEB_HOST = "thdev.app"
        const val APP_WEB_BASE_PATH = "notmid"
    }
}

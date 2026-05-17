package app.thdev.glassnavlab.feature.webview.api

import app.thdev.glassnavlab.core.router.ActivityRoute
import app.thdev.glassnavlab.core.router.ActivityRouteSpec
import app.thdev.glassnavlab.core.router.DeepLinkSpec
import app.thdev.glassnavlab.core.router.RouteStack
import app.thdev.glassnavlab.core.router.WebRoute
import app.thdev.glassnavlab.core.router.WebRouteLink
import java.net.URI

enum class WebViewMode {
    Generic,
    Auth,
    Help,
}

data class WebViewRoute(
    val url: String,
    val title: String? = null,
    val mode: WebViewMode = WebViewMode.Generic,
    val javaScriptEnabled: Boolean = true,
) : ActivityRoute, WebRoute {
    override val route: String = "notmid/web"
    override val activityKey: String = WebViewActivityKeys.DEFAULT
    override val webPathSegments: List<String> = listOf(WEB_PATH)
}

object WebViewActivityKeys {
    const val DEFAULT = "notmid.webview.default"
}

object WebViewRouteSpec : ActivityRouteSpec<WebViewRoute> {
    override val routePattern: String = "notmid/web?url={url}"
    override val activityKey: String = WebViewActivityKeys.DEFAULT

    fun create(
        url: String,
        title: String? = null,
        mode: WebViewMode = WebViewMode.Generic,
        javaScriptEnabled: Boolean = true,
    ): WebViewRoute {
        return WebViewRoute(
            url = url,
            title = title,
            mode = mode,
            javaScriptEnabled = javaScriptEnabled,
        )
    }
}

object WebViewDeepLinkSpec : DeepLinkSpec {
    override val priority: Int = 10

    override fun match(link: WebRouteLink): RouteStack? {
        if (link.pathSegments != listOf(WEB_PATH)) return null

        val url = link.queryParameters["url"]?.firstOrNull()
            ?.takeIf(::isAllowedUrl)
            ?: return null
        val title = link.queryParameters["title"]?.firstOrNull()
        val mode = link.queryParameters["mode"]?.firstOrNull()
            ?.let(::modeFor)
            ?: WebViewMode.Generic
        val javaScriptEnabled = link.queryParameters["js"]?.firstOrNull()
            ?.toBooleanStrictOrNull()
            ?: true

        return RouteStack.single(
            WebViewRouteSpec.create(
                url = url,
                title = title,
                mode = mode,
                javaScriptEnabled = javaScriptEnabled,
            ),
        )
    }

    private fun modeFor(value: String): WebViewMode? {
        return WebViewMode.entries.firstOrNull { mode ->
            mode.name.equals(value, ignoreCase = true)
        }
    }

    private fun isAllowedUrl(value: String): Boolean {
        val uri = runCatching { URI(value) }.getOrNull() ?: return false
        return uri.scheme == "https" || uri.scheme == "http"
    }
}

private const val WEB_PATH = "web"

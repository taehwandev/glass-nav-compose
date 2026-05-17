package app.thdev.glassnavlab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.thdev.glassnavlab.core.router.RouteCommand
import app.thdev.glassnavlab.core.router.RouteEvent
import app.thdev.glassnavlab.core.router.RouteEventSink
import app.thdev.glassnavlab.core.router.Route
import app.thdev.glassnavlab.core.router.RouteStack
import app.thdev.glassnavlab.core.router.Router
import app.thdev.glassnavlab.core.router.ActivityRoute
import app.thdev.glassnavlab.feature.feed.api.FeedRouteEvent
import app.thdev.glassnavlab.feature.inbox.api.InboxRouteEvent
import app.thdev.glassnavlab.feature.map.api.MapRouteEvent
import app.thdev.glassnavlab.feature.notmid.api.NotmidRouteEvent
import app.thdev.glassnavlab.feature.notmid.api.NotmidRoute

@Composable
internal fun rememberAppRouter(): AppRouter {
    return remember { AppRouter() }
}

@Stable
internal class AppRouter : Router, RouteEventSink {
    var backStack: RouteStack by mutableStateOf(
        RouteStack.single(NotmidRouteGraph.defaultRoute),
    )
        private set

    val currentRoute: Route
        get() = backStack.topRoute

    val notmidRouteStack: List<NotmidRoute>
        get() = backStack.entries.filterIsInstance<NotmidRoute>()

    val selectedNotmidDestinationId: String?
        get() = notmidRouteStack.lastOrNull()?.selectedDestinationId

    var pendingActivityRouteRequest: ActivityRouteRequest? by mutableStateOf(null)
        private set

    private var nextActivityRouteRequestId = 0L

    override fun navigate(command: RouteCommand) {
        val activityRoute = command.route as? ActivityRoute
        if (activityRoute != null) {
            pendingActivityRouteRequest = ActivityRouteRequest(
                id = ++nextActivityRouteRequestId,
                route = activityRoute,
            )
            return
        }

        backStack = command.stack
    }

    fun consumeActivityRouteRequest(id: Long) {
        if (pendingActivityRouteRequest?.id == id) {
            pendingActivityRouteRequest = null
        }
    }

    override fun onRouteEvent(event: RouteEvent) {
        when (event) {
            is NotmidRouteEvent -> onNotmidRouteEvent(event)
            is FeedRouteEvent -> onFeedRouteEvent(event)
            is MapRouteEvent -> onMapRouteEvent(event)
            is InboxRouteEvent -> onInboxRouteEvent(event)
        }
    }

    private fun onNotmidRouteEvent(event: NotmidRouteEvent) {
        val stack = when (event) {
            is NotmidRouteEvent.DestinationSelected -> {
                RouteStack.single(NotmidRouteGraph.destination(event.destinationId))
            }

            NotmidRouteEvent.SettingsRequested -> {
                NotmidRouteGraph.settingsStack()
            }
        }

        navigate(RouteCommand(stack = stack))
    }

    private fun onFeedRouteEvent(event: FeedRouteEvent) {
        val stack = when (event) {
            is FeedRouteEvent.ClipRequested -> {
                NotmidRouteGraph.clipStack(event.clipId)
            }
        }

        navigate(RouteCommand(stack = stack))
    }

    private fun onMapRouteEvent(event: MapRouteEvent) {
        val stack = when (event) {
            is MapRouteEvent.PlaceRequested -> {
                NotmidRouteGraph.placeStack(event.placeId)
            }
        }

        navigate(RouteCommand(stack = stack))
    }

    private fun onInboxRouteEvent(event: InboxRouteEvent) {
        val stack = when (event) {
            is InboxRouteEvent.ChatThreadRequested -> {
                NotmidRouteGraph.chatThreadStack(event.threadId)
            }
        }

        navigate(RouteCommand(stack = stack))
    }
}

internal data class ActivityRouteRequest(
    val id: Long,
    val route: ActivityRoute,
)

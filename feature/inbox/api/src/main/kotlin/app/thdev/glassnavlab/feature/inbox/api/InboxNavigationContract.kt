package app.thdev.glassnavlab.feature.inbox.api

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

object InboxRoute : NotmidTopLevelRoute {
    override val route: String = "notmid/inbox"
    override val selectedDestinationId: String = NotmidDestinationIds.INBOX
    override val title: String = "Inbox"
    override val webPathSegments: List<String> = listOf(NotmidDestinationIds.INBOX)
}

object InboxRouteSpec : NotmidTopLevelRouteSpec<InboxRoute> {
    override val route: InboxRoute = InboxRoute
}

object InboxDeepLinkSpec : NotmidStaticDeepLinkSpec(InboxRoute)

data class ChatThreadRoute(
    val threadId: String,
) : NotmidRoute {
    init {
        require(threadId.isNotBlank()) { "threadId must not be blank." }
    }

    override val route: String = "notmid/chats/$threadId"
    override val selectedDestinationId: String = NotmidDestinationIds.INBOX
    override val title: String = "Chat"
    override val webPathSegments: List<String> = listOf(CHATS_PATH, threadId)
}

object ChatThreadRouteSpec : NotmidRouteSpec<ChatThreadRoute> {
    override val routePattern: String = "notmid/chats/{threadId}"

    fun create(threadId: String): ChatThreadRoute {
        return ChatThreadRoute(threadId)
    }
}

object ChatDeepLinkSpec : DeepLinkSpec {
    override val priority: Int = 20

    override fun match(link: WebRouteLink): RouteStack? {
        val threadId = when {
            link.pathSegments.size == 2 && link.pathSegments[0] == CHATS_PATH -> {
                link.pathSegments[1]
            }

            link.pathSegments.size == 3 &&
                link.pathSegments[0] == NotmidDestinationIds.INBOX &&
                link.pathSegments[1] == CHATS_PATH -> {
                link.pathSegments[2]
            }

            else -> return null
        }.takeIf(String::isNotBlank) ?: return null

        return RouteStack.of(
            InboxRoute,
            ChatThreadRouteSpec.create(threadId),
        )
    }
}

sealed interface InboxRouteEvent : RouteEvent {
    data class ChatThreadRequested(
        val threadId: String,
    ) : InboxRouteEvent
}

private const val CHATS_PATH = "chats"

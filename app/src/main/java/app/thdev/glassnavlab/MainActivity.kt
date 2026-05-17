package app.thdev.glassnavlab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import app.thdev.glassnavlab.core.data.notmid.StaticNotmidContentRepository
import app.thdev.glassnavlab.core.designsystem.theme.notmidTheme
import app.thdev.glassnavlab.core.domain.notmid.GetNotmidDestinationsUseCase
import app.thdev.glassnavlab.feature.notmid.NotmidShellScreen
import app.thdev.glassnavlab.navigation.AppActivityRouteLauncher
import app.thdev.glassnavlab.navigation.AppDeepLinkResolver
import app.thdev.glassnavlab.navigation.rememberAppRouter

class MainActivity : ComponentActivity() {
    private var pendingDeepLink by mutableStateOf<PendingDeepLink?>(null)
    private var pendingDeepLinkId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updatePendingDeepLink(intent)
        enableEdgeToEdge()
        setContent {
            val notmidContentRepository = remember { StaticNotmidContentRepository() }
            val notmidDestinations = remember(notmidContentRepository) {
                GetNotmidDestinationsUseCase(notmidContentRepository)()
            }
            val appRouter = rememberAppRouter()
            val deepLinkResolver = remember { AppDeepLinkResolver() }
            val context = LocalContext.current

            LaunchedEffect(pendingDeepLink) {
                val uri = pendingDeepLink?.uri ?: return@LaunchedEffect
                deepLinkResolver.resolve(uri)?.let(appRouter::navigate)
            }

            LaunchedEffect(appRouter.pendingActivityRouteRequest) {
                val request = appRouter.pendingActivityRouteRequest ?: return@LaunchedEffect
                if (AppActivityRouteLauncher.launch(context, request.route)) {
                    appRouter.consumeActivityRouteRequest(request.id)
                }
            }

            notmidTheme {
                NotmidShellScreen(
                    destinations = notmidDestinations,
                    navigationStack = appRouter.notmidRouteStack,
                    onRouteEvent = { event -> appRouter.onRouteEvent(event) },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        updatePendingDeepLink(intent)
    }

    private fun updatePendingDeepLink(intent: Intent?) {
        val uri = intent?.data?.toString() ?: return
        pendingDeepLink = PendingDeepLink(
            uri = uri,
            id = ++pendingDeepLinkId,
        )
    }
}

private data class PendingDeepLink(
    val uri: String,
    val id: Int,
)

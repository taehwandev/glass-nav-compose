package app.thdev.glassnavlab.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.thdev.glassnavlab.core.designsystem.theme.NotmidColorTokens
import app.thdev.glassnavlab.core.designsystem.theme.NotmidTheme
import app.thdev.glassnavlab.core.designsystem.component.NotmidSectionHeader
import app.thdev.glassnavlab.feature.notmid.api.NotmidRoute
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidClipCard
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidDestinationContent
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidPlaceCard
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidClip
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidDestination
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidPlace

@Composable
fun ProfileScreen(
    destination: NotmidDestination,
    listState: LazyListState,
) {
    NotmidDestinationContent(
        destination = destination,
        listState = listState,
    )
}

@Composable
fun ProfileSettingsScreen(
    parentDestination: NotmidDestination,
    navigationStack: List<NotmidRoute>,
    listState: LazyListState,
) {
    val settingsClip = remember(parentDestination.id, navigationStack) {
        NotmidClip(
            id = "profile-settings-route-stack",
            title = "Route stack",
            description = navigationStack.joinToString(" > ") { it.webPathSegments.last() },
            badge = "Web link",
            palette = listOf(
                NotmidColorTokens.Ink,
                NotmidColorTokens.RouteBlue,
                NotmidColorTokens.SignalGreen,
            ),
        )
    }
    val settingsPlace = remember(parentDestination.id) {
        NotmidPlace(
            id = "profile-settings-route",
            title = "${parentDestination.title} Settings",
            description = "Nested route rendered above ${parentDestination.title} for account controls.",
            metric = "Stack",
            palette = listOf(
                NotmidColorTokens.Ink,
                NotmidColorTokens.RouteBlue,
                NotmidColorTokens.SignalGreen,
            ),
            height = 176.dp,
            contentColor = NotmidColorTokens.Cloud,
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            start = NotmidTheme.spacing.screenHorizontal,
            top = NotmidTheme.spacing.screenTop,
            end = NotmidTheme.spacing.screenHorizontal,
            bottom = NotmidTheme.spacing.bottomNavigationPadding,
        ),
        verticalArrangement = Arrangement.spacedBy(NotmidTheme.spacing.lg),
    ) {
        item(key = "settings-header-${parentDestination.id}") {
            NotmidSectionHeader(
                title = "notmid",
                subtitle = navigationStack.joinToString(" > ") { it.webPathSegments.last() },
                eyebrow = "settings",
            )
        }

        item(key = "settings-stack-${parentDestination.id}") {
            NotmidClipCard(clip = settingsClip)
        }

        item(key = "settings-place-${parentDestination.id}") {
            NotmidPlaceCard(
                place = settingsPlace,
                index = 0,
            )
        }

        itemsIndexed(
            items = parentDestination.places,
            key = { index, place -> "settings-${parentDestination.id}-${place.title}-$index" },
        ) { index, place ->
            NotmidPlaceCard(
                place = place,
                index = index + 1,
            )
        }
    }
}

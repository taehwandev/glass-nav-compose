package app.thdev.glassnavlab.feature.notmid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.thdev.glassnavlab.core.designsystem.component.NotmidButton
import app.thdev.glassnavlab.core.designsystem.component.NotmidCard
import app.thdev.glassnavlab.core.designsystem.component.NotmidOutlinedButton
import app.thdev.glassnavlab.core.designsystem.component.NotmidSectionHeader
import app.thdev.glassnavlab.core.designsystem.component.NotmidText
import app.thdev.glassnavlab.core.designsystem.component.NotmidTextVariant
import app.thdev.glassnavlab.core.designsystem.theme.NotmidColorTokens
import app.thdev.glassnavlab.core.designsystem.theme.NotmidTheme
import app.thdev.glassnavlab.core.model.notmid.NotmidAuthRequiredAction
import app.thdev.glassnavlab.core.model.notmid.NotmidAuthState
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidClipCard
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidPlaceCard
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidClip
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidDestination
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidPlace

@Composable
internal fun NotmidLoginScreen(
    destination: NotmidDestination,
    authState: NotmidAuthState,
    listState: LazyListState,
    onContinueLocal: () -> Unit,
    onBrowseSignedOut: () -> Unit,
) {
    val loginClip = remember(destination.id) {
        NotmidClip(
            id = "local-auth-${destination.id}",
            title = "Keep your receipts attached",
            description = "Feed and map stay open. ${destination.title}, saves, chats, and profile edits start behind this local auth boundary.",
            badge = "fake auth",
            palette = listOf(
                NotmidColorTokens.Ink,
                NotmidColorTokens.SignalGreen,
                NotmidColorTokens.RouteBlue,
            ),
        )
    }
    val loginPlace = remember(destination.id, authState.mode) {
        NotmidPlace(
            id = "local-auth-contract-${destination.id}",
            title = "No production Firebase config",
            description = "The app uses a deterministic local session now; Firebase ID tokens can later be verified by apps/api.",
            metric = authState.mode.name.lowercase(),
            palette = listOf(
                NotmidColorTokens.RouteBlue,
                NotmidColorTokens.SignalGreen,
                NotmidColorTokens.WarmClip,
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
        item(key = "login-header-${destination.id}") {
            NotmidSectionHeader(
                title = "notmid account",
                subtitle = "${destination.title} needs a local session before it can write product data.",
                eyebrow = "auth gate",
            )
        }

        item(key = "login-actions-${destination.id}") {
            NotmidCard {
                Column(verticalArrangement = Arrangement.spacedBy(NotmidTheme.spacing.md)) {
                    NotmidText(
                        text = "Continue in local mode",
                        variant = NotmidTextVariant.Headline,
                    )
                    NotmidText(
                        text = authState.requiredActions.joinToString(" / ") { it.label },
                        color = NotmidTheme.colors.contentMuted,
                        variant = NotmidTextVariant.BodySmall,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(NotmidTheme.spacing.sm),
                    ) {
                        NotmidButton(
                            text = "Continue",
                            onClick = onContinueLocal,
                            modifier = Modifier.weight(1f),
                        )
                        NotmidOutlinedButton(
                            text = "Browse",
                            onClick = onBrowseSignedOut,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        item(key = "login-clip-${destination.id}") {
            NotmidClipCard(clip = loginClip)
        }

        item(key = "login-place-${destination.id}") {
            NotmidPlaceCard(
                place = loginPlace,
                index = 0,
            )
        }
    }
}

private val NotmidAuthRequiredAction.label: String
    get() = when (this) {
        NotmidAuthRequiredAction.Capture -> "capture"
        NotmidAuthRequiredAction.Save -> "save"
        NotmidAuthRequiredAction.Chat -> "chat"
        NotmidAuthRequiredAction.ProfileEdit -> "profile edit"
        NotmidAuthRequiredAction.Moderation -> "moderation"
    }

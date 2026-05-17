package app.thdev.glassnavlab.feature.notmid.common.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.thdev.glassnavlab.core.designsystem.component.NotmidGradientSummaryCard
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidClip

@Composable
fun NotmidClipCard(
    clip: NotmidClip,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    NotmidGradientSummaryCard(
        label = clip.badge,
        title = clip.title,
        description = clip.description,
        palette = clip.palette,
        modifier = modifier,
        onClick = onClick,
    )
}

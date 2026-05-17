package app.thdev.glassnavlab.feature.notmid.common.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.thdev.glassnavlab.core.designsystem.component.NotmidGradientHeroCard
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidPlace

@Composable
fun NotmidPlaceCard(
    place: NotmidPlace,
    index: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    NotmidGradientHeroCard(
        title = place.title,
        description = place.description,
        metric = place.metric,
        palette = place.palette,
        height = place.height,
        contentColor = place.contentColor,
        emphasizedTitle = index % 3 == 1,
        modifier = modifier,
        onClick = onClick,
    )
}

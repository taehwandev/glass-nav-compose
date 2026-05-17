package app.thdev.glassnavlab.feature.notmid.common.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.thdev.glassnavlab.core.designsystem.theme.NotmidColorTokens
import app.thdev.glassnavlab.core.model.notmid.NotmidColor
import app.thdev.glassnavlab.core.model.notmid.NotmidDestination as NotmidDestinationModel
import app.thdev.glassnavlab.core.model.notmid.NotmidNavigationIcon
import app.thdev.glassnavlab.core.model.notmid.NotmidClip as NotmidClipModel
import app.thdev.glassnavlab.core.model.notmid.NotmidPlace as NotmidPlaceModel

data class NotmidDestination(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: NotmidNavigationIcon,
    val clips: List<NotmidClip>,
    val places: List<NotmidPlace>,
)

data class NotmidClip(
    val id: String,
    val title: String,
    val description: String,
    val badge: String,
    val palette: List<Color>,
)

data class NotmidPlace(
    val id: String,
    val title: String,
    val description: String,
    val metric: String,
    val palette: List<Color>,
    val height: Dp,
    val contentColor: Color,
)

val NotmidBackgroundColor = NotmidColorTokens.WarmMist

fun List<NotmidDestinationModel>.toNotmidDestinations(): List<NotmidDestination> {
    return map { it.toUi() }
}

fun destinationFor(
    destinations: List<NotmidDestination>,
    selectedItemId: String,
): NotmidDestination {
    return destinations.firstOrNull { it.id == selectedItemId } ?: destinations.first()
}

fun backdropPaletteForItem(
    destination: NotmidDestination,
    itemIndex: Int,
): List<Color>? {
    val contentIndex = itemIndex - 1
    if (contentIndex < 0) return null

    return when {
        contentIndex < destination.clips.size -> destination.clips[contentIndex].palette
        else -> {
            val placeIndex = contentIndex - destination.clips.size
            destination.places.getOrNull(placeIndex)?.palette
        }
    }
}

fun notmidPalette(
    palette: List<Color>,
    fraction: Float,
): Color {
    if (palette.isEmpty()) return NotmidBackgroundColor
    if (palette.size == 1) return palette.first()

    val scaledFraction = fraction.coerceIn(0f, 1f) * palette.lastIndex
    val startIndex = scaledFraction.toInt().coerceIn(0, palette.lastIndex - 1)
    val endIndex = startIndex + 1
    return lerpColor(
        start = palette[startIndex],
        stop = palette[endIndex],
        fraction = scaledFraction - startIndex,
    )
}

private fun NotmidDestinationModel.toUi(): NotmidDestination {
    return NotmidDestination(
        id = id,
        title = title,
        subtitle = subtitle,
        icon = icon,
        clips = clips.map(NotmidClipModel::toUi),
        places = places.map(NotmidPlaceModel::toUi),
    )
}

private fun NotmidClipModel.toUi(): NotmidClip {
    return NotmidClip(
        id = id,
        title = title,
        description = description,
        badge = badge,
        palette = palette.map(NotmidColor::toColor),
    )
}

private fun NotmidPlaceModel.toUi(): NotmidPlace {
    return NotmidPlace(
        id = id,
        title = title,
        description = description,
        metric = metric,
        palette = palette.map(NotmidColor::toColor),
        height = heightDp.dp,
        contentColor = contentColor.toColor(),
    )
}

private fun NotmidColor.toColor(): Color = Color(argb)

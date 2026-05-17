package app.thdev.glassnavlab.feature.notmid.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.thdev.glassnavlab.core.designsystem.theme.NotmidTheme
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidClip
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidDestination
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidPlace

@Composable
fun NotmidDestinationContent(
    destination: NotmidDestination,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onClipSelected: ((NotmidClip) -> Unit)? = null,
    onPlaceSelected: ((NotmidPlace) -> Unit)? = null,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            start = NotmidTheme.spacing.screenHorizontal,
            top = NotmidTheme.spacing.screenTop,
            end = NotmidTheme.spacing.screenHorizontal,
            bottom = NotmidTheme.spacing.bottomNavigationPadding,
        ),
        verticalArrangement = Arrangement.spacedBy(NotmidTheme.spacing.lg),
    ) {
        item(key = "header-${destination.id}") {
            NotmidHeader(destination = destination)
        }

        itemsIndexed(
            items = destination.clips,
            key = { index, clip -> "${destination.id}-clip-${clip.title}-$index" },
        ) { _, clip ->
            NotmidClipCard(
                clip = clip,
                onClick = onClipSelected?.let { callback -> { callback(clip) } },
            )
        }

        itemsIndexed(
            items = destination.places,
            key = { index, place -> "${destination.id}-place-${place.title}-$index" },
        ) { index, place ->
            NotmidPlaceCard(
                place = place,
                index = index,
                onClick = onPlaceSelected?.let { callback -> { callback(place) } },
            )
        }
    }
}

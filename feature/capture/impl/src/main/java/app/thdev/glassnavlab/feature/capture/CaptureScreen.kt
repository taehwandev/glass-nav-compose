package app.thdev.glassnavlab.feature.capture

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import app.thdev.glassnavlab.feature.notmid.common.components.NotmidDestinationContent
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidDestination

@Composable
fun CaptureScreen(
    destination: NotmidDestination,
    listState: LazyListState,
) {
    NotmidDestinationContent(
        destination = destination,
        listState = listState,
    )
}

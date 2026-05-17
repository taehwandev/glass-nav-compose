package app.thdev.glassnavlab.feature.notmid.common.components

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.thdev.glassnavlab.core.designsystem.component.NotmidMetricTile
import app.thdev.glassnavlab.core.designsystem.component.NotmidSectionHeader
import app.thdev.glassnavlab.core.designsystem.theme.NotmidTheme
import app.thdev.glassnavlab.feature.notmid.common.model.NotmidDestination

@Composable
fun NotmidHeader(destination: NotmidDestination) {
    Column(verticalArrangement = Arrangement.spacedBy(NotmidTheme.spacing.lg)) {
        NotmidSectionHeader(
            title = "notmid",
            subtitle = destination.subtitle,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(NotmidTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NotmidMetricTile(
                label = "Mode",
                value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    "AGSL"
                } else {
                    "Legacy"
                },
                modifier = Modifier.weight(1f),
            )
            NotmidMetricTile(
                label = "Clips",
                value = destination.clips.size.toString(),
                modifier = Modifier.weight(1f),
            )
            NotmidMetricTile(
                label = "Places",
                value = destination.places.size.toString(),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

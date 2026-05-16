package app.thdev.glassnavlab.ui.demo.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.thdev.glassnavlab.ui.demo.model.DemoDestination

@Composable
internal fun DemoHeader(destination: DemoDestination) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Glass Nav Lab",
                color = Color(0xFF171717),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = destination.subtitle,
                color = Color(0xFF575757),
                fontSize = 15.sp,
                lineHeight = 20.sp,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DemoStatusTile(
                label = "Renderer",
                value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    "AGSL"
                } else {
                    "Legacy"
                },
                modifier = Modifier.weight(1f),
            )
            DemoStatusTile(
                label = "Samples",
                value = destination.samples.size.toString(),
                modifier = Modifier.weight(1f),
            )
            DemoStatusTile(
                label = "Surfaces",
                value = destination.surfaces.size.toString(),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun DemoStatusTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.72f))
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = label,
            color = Color(0xFF6B6F74),
            fontSize = 11.sp,
            lineHeight = 12.sp,
            maxLines = 1,
        )
        Text(
            text = value,
            color = Color(0xFF171717),
            fontSize = 14.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
    }
}

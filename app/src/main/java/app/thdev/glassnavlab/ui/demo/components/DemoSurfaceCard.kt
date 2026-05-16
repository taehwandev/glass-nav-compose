package app.thdev.glassnavlab.ui.demo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.thdev.glassnavlab.ui.demo.model.DemoSurface

@Composable
internal fun DemoSurfaceCard(
    surface: DemoSurface,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(surface.height)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(surface.palette))
            .padding(22.dp),
    ) {
        Text(
            text = surface.metric,
            modifier = Modifier.align(Alignment.TopEnd),
            color = surface.contentColor.copy(alpha = 0.72f),
            fontSize = 13.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
        Column(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = surface.title,
                color = surface.contentColor,
                fontSize = if (index % 3 == 1) 25.sp else 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = surface.description,
                color = surface.contentColor.copy(alpha = 0.76f),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

package app.thdev.glassnavlab.feature.notmid.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import app.thdev.glassnavlab.core.model.notmid.NotmidNavigationIcon

@Composable
fun NotmidGlassIcon(type: NotmidNavigationIcon, color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(
            width = 2.1.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        )
        when (type) {
            NotmidNavigationIcon.Feed -> {
                val play = Path().apply {
                    moveTo(size.width * 0.34f, size.height * 0.26f)
                    lineTo(size.width * 0.34f, size.height * 0.74f)
                    lineTo(size.width * 0.74f, size.height * 0.50f)
                    close()
                }
                drawRoundRect(
                    color = color,
                    topLeft = Offset(size.width * 0.20f, size.height * 0.18f),
                    size = Size(size.width * 0.60f, size.height * 0.64f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        x = size.minDimension * 0.16f,
                        y = size.minDimension * 0.16f,
                    ),
                    style = stroke,
                )
                drawPath(play, color, style = stroke)
            }

            NotmidNavigationIcon.Map -> {
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.14f,
                    center = Offset(size.width * 0.50f, size.height * 0.38f),
                    style = stroke,
                )
                val pin = Path().apply {
                    moveTo(size.width * 0.50f, size.height * 0.78f)
                    cubicTo(
                        size.width * 0.24f,
                        size.height * 0.56f,
                        size.width * 0.24f,
                        size.height * 0.28f,
                        size.width * 0.50f,
                        size.height * 0.20f,
                    )
                    cubicTo(
                        size.width * 0.76f,
                        size.height * 0.28f,
                        size.width * 0.76f,
                        size.height * 0.56f,
                        size.width * 0.50f,
                        size.height * 0.78f,
                    )
                }
                drawPath(pin, color, style = stroke)
            }

            NotmidNavigationIcon.Capture -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(size.width * 0.22f, size.height * 0.28f),
                    size = Size(size.width * 0.56f, size.height * 0.44f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        x = size.minDimension * 0.10f,
                        y = size.minDimension * 0.10f,
                    ),
                    style = stroke,
                )
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.11f,
                    center = Offset(size.width * 0.50f, size.height * 0.50f),
                    style = stroke,
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.36f, size.height * 0.28f),
                    end = Offset(size.width * 0.42f, size.height * 0.20f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
            }

            NotmidNavigationIcon.Inbox -> {
                val bubble = Path().apply {
                    moveTo(size.width * 0.24f, size.height * 0.24f)
                    lineTo(size.width * 0.76f, size.height * 0.24f)
                    lineTo(size.width * 0.76f, size.height * 0.62f)
                    lineTo(size.width * 0.56f, size.height * 0.62f)
                    lineTo(size.width * 0.42f, size.height * 0.78f)
                    lineTo(size.width * 0.42f, size.height * 0.62f)
                    lineTo(size.width * 0.24f, size.height * 0.62f)
                    close()
                }
                drawPath(bubble, color, style = stroke)
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.36f, size.height * 0.40f),
                    end = Offset(size.width * 0.64f, size.height * 0.40f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
            }

            NotmidNavigationIcon.Profile -> {
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.16f,
                    center = Offset(size.width * 0.50f, size.height * 0.35f),
                    style = stroke,
                )
                drawArc(
                    color = color,
                    startAngle = 205f,
                    sweepAngle = 130f,
                    useCenter = false,
                    topLeft = Offset(size.width * 0.26f, size.height * 0.52f),
                    size = Size(size.width * 0.48f, size.height * 0.38f),
                    style = stroke,
                )
            }
        }
    }
}

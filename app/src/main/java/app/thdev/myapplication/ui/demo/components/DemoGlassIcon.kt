package app.thdev.myapplication.ui.demo.components

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
import app.thdev.myapplication.ui.demo.model.GlassNavIcon

@Composable
internal fun DemoGlassIcon(type: GlassNavIcon, color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(
            width = 2.1.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        )
        when (type) {
            GlassNavIcon.Home -> {
                val roof = Path().apply {
                    moveTo(size.width * 0.18f, size.height * 0.48f)
                    lineTo(size.width * 0.50f, size.height * 0.22f)
                    lineTo(size.width * 0.82f, size.height * 0.48f)
                }
                val body = Path().apply {
                    moveTo(size.width * 0.28f, size.height * 0.46f)
                    lineTo(size.width * 0.28f, size.height * 0.78f)
                    lineTo(size.width * 0.72f, size.height * 0.78f)
                    lineTo(size.width * 0.72f, size.height * 0.46f)
                }
                drawPath(roof, color, style = stroke)
                drawPath(body, color, style = stroke)
            }

            GlassNavIcon.Search -> {
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.26f,
                    center = Offset(size.width * 0.43f, size.height * 0.42f),
                    style = stroke,
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.61f, size.height * 0.61f),
                    end = Offset(size.width * 0.80f, size.height * 0.80f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
            }

            GlassNavIcon.Create -> {
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.50f, size.height * 0.22f),
                    end = Offset(size.width * 0.50f, size.height * 0.78f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.22f, size.height * 0.50f),
                    end = Offset(size.width * 0.78f, size.height * 0.50f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
            }

            GlassNavIcon.Profile -> {
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

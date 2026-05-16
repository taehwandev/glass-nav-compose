package app.thdev.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.thdev.myapplication.ui.components.liquidglass.LiquidGlassBackdropHost
import app.thdev.myapplication.ui.components.liquidglass.LiquidGlassBottomNavigation
import app.thdev.myapplication.ui.components.liquidglass.LiquidGlassNavigationAction
import app.thdev.myapplication.ui.components.liquidglass.LiquidGlassNavigationItem
import app.thdev.myapplication.ui.components.liquidglass.rememberLiquidGlassNavigationState
import app.thdev.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LiquidGlassDemo()
            }
        }
    }
}

@Composable
private fun LiquidGlassDemo() {
    val items = rememberDemoNavigationItems()
    val navigationState = rememberLiquidGlassNavigationState(
        initialSelectedItemId = items.first().id,
    )
    val listState = rememberLazyListState()
    val navigationBackdropColor by rememberNavigationBackdropColor(
        listState = listState,
        selectedItemId = navigationState.selectedItemId,
    )

    LiquidGlassBackdropHost(
        modifier = Modifier
            .fillMaxSize()
            .background(DemoBackgroundColor),
        backgroundColor = DemoBackgroundColor,
        content = {
            DemoContent(
                selectedItemId = navigationState.selectedItemId,
                listState = listState,
            )
        },
        floatingContent = { backdrop ->
            LiquidGlassBottomNavigation(
                items = items,
                state = navigationState,
                backdrop = backdrop,
                modifier = Modifier.align(Alignment.BottomCenter),
                adaptiveBackgroundColor = navigationBackdropColor,
                trailingAction = LiquidGlassNavigationAction(
                    contentDescription = "Create",
                    icon = { color -> DemoGlassIcon(GlassNavIcon.Create, color) },
                    onClick = { navigationState.select("create") },
                ),
            )
        },
    )
}

@Composable
private fun rememberDemoNavigationItems(): List<LiquidGlassNavigationItem> {
    return remember {
        listOf(
            LiquidGlassNavigationItem(
                id = "home",
                label = "Home",
                icon = { _, color -> DemoGlassIcon(GlassNavIcon.Home, color) },
            ),
            LiquidGlassNavigationItem(
                id = "search",
                label = "Search",
                icon = { _, color -> DemoGlassIcon(GlassNavIcon.Search, color) },
            ),
            LiquidGlassNavigationItem(
                id = "create",
                label = "Create",
                icon = { _, color -> DemoGlassIcon(GlassNavIcon.Create, color) },
            ),
            LiquidGlassNavigationItem(
                id = "profile",
                label = "Profile",
                icon = { _, color -> DemoGlassIcon(GlassNavIcon.Profile, color) },
            ),
        )
    }
}

@Composable
private fun DemoContent(
    selectedItemId: String,
    listState: LazyListState,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 64.dp,
            end = 20.dp,
            bottom = 150.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Liquid Glass",
                    color = Color(0xFF171717),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        "AGSL shader version"
                    } else {
                        "Legacy fallback version"
                    },
                    color = Color(0xFF555555),
                    fontSize = 16.sp,
                )
            }
        }

        items(14) { index ->
            DemoCard(
                index = index,
                selectedOffset = selectedOffset(selectedItemId),
            )
        }
    }
}

@Composable
private fun rememberNavigationBackdropColor(
    listState: LazyListState,
    selectedItemId: String,
): State<Color> {
    val density = LocalDensity.current
    val sampleOffsetFromBottom = with(density) { 88.dp.roundToPx() }

    return remember(listState, selectedItemId, sampleOffsetFromBottom) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val sampleY = layoutInfo.viewportEndOffset - sampleOffsetFromBottom
            val visibleCard = layoutInfo.visibleItemsInfo.firstOrNull { item ->
                item.index > 0 && sampleY >= item.offset && sampleY <= item.offset + item.size
            } ?: return@derivedStateOf DemoBackgroundColor

            val cardIndex = visibleCard.index - 1
            val localFraction = ((sampleY - visibleCard.offset).toFloat() / visibleCard.size)
                .coerceIn(0f, 1f)
            sampleDemoPalette(
                palette = demoPalettes[
                    (cardIndex + selectedOffset(selectedItemId)) % demoPalettes.size
                ],
                fraction = localFraction,
            )
        }
    }
}

private fun sampleDemoPalette(
    palette: List<Color>,
    fraction: Float,
): Color {
    if (palette.isEmpty()) return DemoBackgroundColor
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

@Composable
private fun DemoCard(index: Int, selectedOffset: Int) {
    val palette = demoPalettes[(index + selectedOffset) % demoPalettes.size]
    val height = when (index % 4) {
        0 -> 180.dp
        1 -> 132.dp
        2 -> 154.dp
        else -> 116.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(palette))
            .padding(22.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Surface ${index + 1}",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Scroll behind the bar to see the material adapt.",
                color = Color.White.copy(alpha = 0.78f),
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
private fun DemoGlassIcon(type: GlassNavIcon, color: Color) {
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

private fun selectedOffset(selectedItemId: String): Int {
    return when (selectedItemId) {
        "home" -> 0
        "search" -> 1
        "create" -> 2
        "profile" -> 3
        else -> 0
    }
}

private enum class GlassNavIcon {
    Home,
    Search,
    Create,
    Profile,
}

private val DemoBackgroundColor = Color(0xFFF3F1EC)

private val demoPalettes = listOf(
    listOf(Color(0xFFFA705A), Color(0xFF7A4EF3), Color(0xFF232A7C)),
    listOf(Color(0xFF0E7C66), Color(0xFF32D6A2), Color(0xFFF6D66B)),
    listOf(Color(0xFF101820), Color(0xFF31495E), Color(0xFFC8D7E0)),
    listOf(Color(0xFFEB4965), Color(0xFFF7A35C), Color(0xFFFFE4AA)),
    listOf(Color(0xFF2F6BFF), Color(0xFF7FE3FF), Color(0xFFFFFFFF)),
    listOf(Color(0xFF262626), Color(0xFF8D8D8D), Color(0xFFF3F3F3)),
)

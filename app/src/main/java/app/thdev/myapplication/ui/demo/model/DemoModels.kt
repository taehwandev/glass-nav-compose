package app.thdev.myapplication.ui.demo.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal data class DemoDestination(
    val id: String,
    val title: String,
    val subtitle: String,
    val samples: List<DemoSample>,
    val surfaces: List<DemoSurface>,
)

internal data class DemoSample(
    val title: String,
    val description: String,
    val badge: String,
    val palette: List<Color>,
)

internal data class DemoSurface(
    val title: String,
    val description: String,
    val metric: String,
    val palette: List<Color>,
    val height: Dp,
    val contentColor: Color = Color.White,
)

internal enum class GlassNavIcon {
    Home,
    Search,
    Create,
    Profile,
}

internal val DemoBackgroundColor = Color(0xFFF3F1EC)

internal fun destinationFor(selectedItemId: String): DemoDestination {
    return DemoDestinations.firstOrNull { it.id == selectedItemId } ?: DemoDestinations.first()
}

internal fun backdropPaletteForItem(
    destination: DemoDestination,
    itemIndex: Int,
): List<Color>? {
    val contentIndex = itemIndex - 1
    if (contentIndex < 0) return null

    return when {
        contentIndex < destination.samples.size -> destination.samples[contentIndex].palette
        else -> {
            val surfaceIndex = contentIndex - destination.samples.size
            destination.surfaces.getOrNull(surfaceIndex)?.palette
        }
    }
}

internal fun sampleDemoPalette(
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

private val DarkCardContent = Color(0xFF17202A)

private val DemoDestinations = listOf(
    DemoDestination(
        id = "home",
        title = "Home",
        subtitle = "A richer feed for checking the navigation over changing surfaces.",
        samples = listOf(
            DemoSample(
                title = "Stateful bottom navigation",
                description = "Saved state holder for app screens.",
                badge = "Sample 01",
                palette = listOf(Color(0xFF2F6BFF), Color(0xFF7FE3FF), Color(0xFFFFFFFF)),
            ),
            DemoSample(
                title = "Trailing glass action",
                description = "Circular plus action with shared glass.",
                badge = "Sample 02",
                palette = listOf(Color(0xFF0E7C66), Color(0xFF32D6A2), Color(0xFFF6D66B)),
            ),
            DemoSample(
                title = "Two bucket adaptation",
                description = "Stable light and dark tone buckets.",
                badge = "Sample 03",
                palette = listOf(Color(0xFF101820), Color(0xFF31495E), Color(0xFFC8D7E0)),
            ),
        ),
        surfaces = listOf(
            DemoSurface(
                title = "Adaptive Surface",
                description = "The bar samples the lower viewport and switches between two readable tones.",
                metric = "Tone",
                palette = listOf(Color(0xFFFA705A), Color(0xFF7A4EF3), Color(0xFF232A7C)),
                height = 180.dp,
            ),
            DemoSurface(
                title = "Shared Action",
                description = "The trailing plus button uses the same glass language as the main bar.",
                metric = "Action",
                palette = listOf(Color(0xFF0E7C66), Color(0xFF32D6A2), Color(0xFFF6D66B)),
                height = 138.dp,
            ),
            DemoSurface(
                title = "Dark Backdrop",
                description = "Dark content keeps the menu readable without forcing a full black overlay.",
                metric = "Dark",
                palette = listOf(Color(0xFF101820), Color(0xFF31495E), Color(0xFFC8D7E0)),
                height = 154.dp,
            ),
            DemoSurface(
                title = "Bright Backdrop",
                description = "Bright content fades the inactive labels so the selected item stays dominant.",
                metric = "Bright",
                palette = listOf(Color(0xFF2F6BFF), Color(0xFF7FE3FF), Color(0xFFFFFFFF)),
                height = 132.dp,
                contentColor = DarkCardContent,
            ),
        ),
    ),
    DemoDestination(
        id = "search",
        title = "Search",
        subtitle = "A search-oriented example with result states behind the same glass navigation.",
        samples = listOf(
            DemoSample(
                title = "Stateless selectedItemId",
                description = "Parent owns selectedItemId.",
                badge = "Sample 04",
                palette = listOf(Color(0xFF262626), Color(0xFF8D8D8D), Color(0xFFF3F3F3)),
            ),
            DemoSample(
                title = "Dense labels",
                description = "Inactive labels fade behind selection.",
                badge = "Sample 05",
                palette = listOf(Color(0xFF005E7C), Color(0xFF5AD7CF), Color(0xFFF7E37B)),
            ),
            DemoSample(
                title = "Search results backdrop",
                description = "Mixed cards for blur and contrast.",
                badge = "Sample 06",
                palette = listOf(Color(0xFFF65868), Color(0xFFB15FF4), Color(0xFF593AE8)),
            ),
        ),
        surfaces = listOf(
            DemoSurface(
                title = "Recent Queries",
                description = "Dense list content stays legible while the bottom bar floats above it.",
                metric = "12",
                palette = listOf(Color(0xFF262626), Color(0xFF8D8D8D), Color(0xFFF3F3F3)),
                height = 156.dp,
            ),
            DemoSurface(
                title = "Result Cluster",
                description = "Grouped content lets the bar prove its blur and refraction on mixed color.",
                metric = "48",
                palette = listOf(Color(0xFF005E7C), Color(0xFF5AD7CF), Color(0xFFF7E37B)),
                height = 180.dp,
            ),
            DemoSurface(
                title = "Focused Match",
                description = "High contrast foregrounds make selection state easy to compare.",
                metric = "Live",
                palette = listOf(Color(0xFF111827), Color(0xFF3B4A6B), Color(0xFFB9C7D6)),
                height = 124.dp,
            ),
            DemoSurface(
                title = "Saved Filter",
                description = "The component API stays stateless when the parent owns selectedItemId.",
                metric = "API",
                palette = listOf(Color(0xFFF65868), Color(0xFFB15FF4), Color(0xFF593AE8)),
                height = 148.dp,
            ),
        ),
    ),
    DemoDestination(
        id = "create",
        title = "Create",
        subtitle = "A creation flow showing flat settled selection and glass motion states.",
        samples = listOf(
            DemoSample(
                title = "Flat settled selection",
                description = "Selected pill rests flat when idle.",
                badge = "Sample 07",
                palette = listOf(Color(0xFFEB4965), Color(0xFFF7A35C), Color(0xFFFFE4AA)),
            ),
            DemoSample(
                title = "Glass while dragging",
                description = "Long press restores the glass capsule.",
                badge = "Sample 08",
                palette = listOf(Color(0xFF2D5BE3), Color(0xFF26D0CE), Color(0xFFF1F7B5)),
            ),
            DemoSample(
                title = "Circular press target",
                description = "Plus scales without square ripple.",
                badge = "Sample 09",
                palette = listOf(Color(0xFF0D3B2E), Color(0xFF1B9C85), Color(0xFFCCE8CC)),
            ),
        ),
        surfaces = listOf(
            DemoSurface(
                title = "Draft Setup",
                description = "The selected pill rests flat, then becomes glass while moving.",
                metric = "01",
                palette = listOf(Color(0xFFEB4965), Color(0xFFF7A35C), Color(0xFFFFE4AA)),
                height = 150.dp,
            ),
            DemoSurface(
                title = "Media Stack",
                description = "Large vivid blocks reveal whether backdrop sampling reacts naturally.",
                metric = "02",
                palette = listOf(Color(0xFF2D5BE3), Color(0xFF26D0CE), Color(0xFFF1F7B5)),
                height = 190.dp,
                contentColor = DarkCardContent,
            ),
            DemoSurface(
                title = "Review State",
                description = "The bar keeps controls visible over mid-tone gradients.",
                metric = "03",
                palette = listOf(Color(0xFF313B72), Color(0xFF9C5FD5), Color(0xFFE9B5CA)),
                height = 132.dp,
            ),
            DemoSurface(
                title = "Publish Ready",
                description = "A separate round action can trigger the same destination.",
                metric = "04",
                palette = listOf(Color(0xFF0D3B2E), Color(0xFF1B9C85), Color(0xFFCCE8CC)),
                height = 156.dp,
            ),
        ),
    ),
    DemoDestination(
        id = "profile",
        title = "Profile",
        subtitle = "A profile dashboard example with neutral, dark, and bright backdrop tests.",
        samples = listOf(
            DemoSample(
                title = "Design components",
                description = "Header, sample, surface, icon split out.",
                badge = "Sample 10",
                palette = listOf(Color(0xFF111111), Color(0xFF555555), Color(0xFFEDEDED)),
            ),
            DemoSample(
                title = "Central model data",
                description = "Samples and surfaces live in model.",
                badge = "Sample 11",
                palette = listOf(Color(0xFF3D4D57), Color(0xFF99AAB5), Color(0xFFE5ECEF)),
            ),
            DemoSample(
                title = "Release-ready docs",
                description = "Docs point to key code paths.",
                badge = "Sample 12",
                palette = listOf(Color(0xFF172A3A), Color(0xFF2E8BC0), Color(0xFFB1D4E0)),
            ),
        ),
        surfaces = listOf(
            DemoSurface(
                title = "Usage Snapshot",
                description = "Neutral cards make inactive label contrast easier to judge.",
                metric = "87%",
                palette = listOf(Color(0xFF111111), Color(0xFF555555), Color(0xFFEDEDED)),
                height = 154.dp,
            ),
            DemoSurface(
                title = "Session Quality",
                description = "The AGSL sheen remains subtle over grayscale content.",
                metric = "A",
                palette = listOf(Color(0xFF3D4D57), Color(0xFF99AAB5), Color(0xFFE5ECEF)),
                height = 130.dp,
                contentColor = DarkCardContent,
            ),
            DemoSurface(
                title = "Saved Layout",
                description = "Component styling is centralized in LiquidGlassNavigationStyle.",
                metric = "Style",
                palette = listOf(Color(0xFF6930C3), Color(0xFF64DFDF), Color(0xFFFFF3B0)),
                height = 176.dp,
            ),
            DemoSurface(
                title = "Release Check",
                description = "Screenshots and sample data are kept with the demo app.",
                metric = "Docs",
                palette = listOf(Color(0xFF172A3A), Color(0xFF2E8BC0), Color(0xFFB1D4E0)),
                height = 144.dp,
            ),
        ),
    ),
)

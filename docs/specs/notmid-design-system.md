# notmid Design System

## Purpose

The design system is the shared source of truth for notmid's Android UI and Stitch/Web design direction.

It should keep the product from becoming a loose sample app:

- product tokens live in `:core:designsystem`
- feature UI consumes tokens through `NotmidTheme`
- feature modules keep product data, route events, and screen composition
- Stitch prompts use the same color, type, shape, and component language

No API keys, Firebase project IDs, service accounts, or MCP credentials belong in this document.

## Code Ownership

```text
:core:designsystem
  theme/
    Color.kt
    Type.kt
    Spacing.kt
    Shape.kt
    Elevation.kt
    Theme.kt
  component/
    NotmidActionRail
    NotmidBottomNavigation
    NotmidButton
    NotmidCheckbox
    NotmidCard
    NotmidFloatingActionButton
    NotmidGlassSurface
    NotmidGradientHeroCard
    NotmidGradientSummaryCard
    NotmidHorizontalDivider
    NotmidIconButton
    NotmidMetricTile
    NotmidOutlinedButton
    NotmidPillButton
    NotmidScaffold
    NotmidSnackbarHost
    NotmidSurface
    NotmidSwitch
    NotmidSectionHeader
    NotmidText
    NotmidTextButton
    NotmidTextField
    NotmidTopAppBar
    liquidglass/*

:feature:notmid:common
  product-shaped cards and screen sections built from design-system tokens
```

Use:

```kotlin
notmidTheme {
    val colors = NotmidTheme.colors
    val type = NotmidTheme.typography
    val spacing = NotmidTheme.spacing
}
```

## Color Tokens

Base:

```text
Ink         #101114
Slate       #2D333B
Muted       #575D64
Subtle      #6B7178
Mist        #F4F6F8
Warm Mist   #F3F1EC
Cloud       #FFFFFF
Line        rgba(16,17,20,0.10)
```

Accents:

```text
Signal Green #14B87A
Route Blue   #2F6BFF
Warm Clip    #FF704D
Night Violet #7A4EF3
Alert Red    #F04452
```

Glass:

```text
Light Glass        rgba(255,255,255,0.68)
Light Glass Strong rgba(255,255,255,0.85)
Dark Glass         rgba(18,20,24,0.56)
Glass Stroke       rgba(255,255,255,0.35)
Shadow             rgba(0,0,0,0.18)
```

Usage:

- `background`: warm off-white app chrome.
- `surfaceRaised`: cards and tiles on app chrome.
- `glassLight`: map/feed controls and floating cards.
- `glassDark`: overlays on high-luminance video or media.
- `signal`, `route`, `clip`, `night`, `danger`: state/action accents, not full-page themes.

## Typography

Use a system sans stack. Android currently uses `FontFamily.Default`; web/Stitch can use Inter or SF Pro.

```text
Display  34/38 weight 700
Title    22/28 weight 700
Headline 18/20 weight 600
Body     15/22 weight 400
BodySmall 13/18 weight 400
Label    12/16 weight 600
Caption  11/14 weight 500
```

Letter spacing is always `0`.

## Spacing

```text
xxs 3
xs  6
sm  8
md  12
lg  16
xl  20
xxl 24

screenHorizontal 20
screenTop        64
bottomNavPadding 150
bottomNavSample  88
```

Fixed-format UI such as bottom navigation, clip cards, metric tiles, and media cards should keep stable dimensions.

## Shape

```text
control   8
card      16
cardLarge 24
sheet     28
pill      999
```

Use 8-16dp radius for dense cards and controls. Reserve 20-28dp for floating sheets, media cards, and glass surfaces.

## Components

Material3 wrappers:

```text
NotmidText
NotmidTextField
NotmidButton
NotmidOutlinedButton
NotmidTextButton
NotmidIconButton
NotmidCheckbox
NotmidSwitch
NotmidScaffold
NotmidTopAppBar
NotmidCenterAlignedTopAppBar
NotmidLargeTopAppBar
NotmidSnackbarHost
NotmidFloatingActionButton
NotmidSurface
NotmidCard
NotmidHorizontalDivider
```

The app should import Material3 directly only inside `:core:designsystem`. Feature and app modules should use `Notmid*` components unless they have a clear platform-level reason.

Product primitives:

```text
NotmidBottomNavigation
NotmidGlassSurface
NotmidMetricTile
NotmidPillButton
NotmidActionRail
NotmidSectionHeader
NotmidGradientSummaryCard
NotmidGradientHeroCard
```

Liquid Glass primitives:

```text
LiquidGlassBottomNavigation
LiquidGlassBackdropHost
```

Feature common components:

```text
NotmidHeader
NotmidClipCard
NotmidPlaceCard
NotmidDestinationContent
NotmidRouteDetailContent
```

Feature cards should adapt product models into design-system components, not own reusable card styling.

```text
NotmidClipCard -> NotmidGradientSummaryCard
NotmidPlaceCard -> NotmidGradientHeroCard
Notmid shell bottom navigation -> NotmidBottomNavigation
```

Expected future components:

```text
PlacePreviewSheet
CaptureComposer
ChatComposer
RichMessageBubble
AuthGate
MapPin
```

`NotmidActionRail` already covers the generic vertical media action pattern. Feed-specific labels, counts, and callbacks should be provided by feature modules.

## Stitch Direction

When generating web/app design in Stitch, use the same token names and component roles.

Prompt addition:

```text
Use the notmid design system:
- Ink #101114, Warm Mist #F3F1EC, Mist #F4F6F8, Cloud #FFFFFF
- Signal Green #14B87A, Route Blue #2F6BFF, Warm Clip #FF704D, Night Violet #7A4EF3, Alert Red #F04452
- Light Glass rgba(255,255,255,0.68), Dark Glass rgba(18,20,24,0.56), Glass Stroke rgba(255,255,255,0.35)
- Typography: Display 34/38 700, Title 22/28 700, Body 15/22 400, Label 12/16 600, Caption 11/14 500
- Radius: controls 8, cards 16, floating glass cards 24, sheets 28
- Use glass only for functional surfaces: navigation, cards, controls, composer, sheets
- Do not use decorative blobs, single-hue purple gradients, oversized marketing heroes, or nested cards
```

## Implementation Rules

- Use `NotmidTheme` in Compose UI instead of hard-coded text styles, app chrome colors, spacing, or surface shapes.
- Keep media-derived palettes in data/UI models because they come from clips and places.
- Keep domain models free of Compose `Color`, `Dp`, and typography types.
- Do not turn `:core:designsystem` into a product data module. It owns presentation primitives only.
- Keep `dynamicColor = false` by default so Android Material You does not overwrite the product identity.

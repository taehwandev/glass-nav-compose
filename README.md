# glass-nav-compose

Adaptive glass bottom navigation for Jetpack Compose.

This repository currently contains a demo Android app that experiments with a Liquid Glass-inspired bottom navigation pattern using Compose and AGSL-capable backdrop rendering.

## Features

- Jetpack Compose bottom navigation component
- AGSL/backdrop-based glass rendering on supported Android versions
- Legacy blur fallback for older Android versions
- Two-step light/dark material adaptation based on background luminance
- Flat selected pill after selection settles
- Glass transition while pressing, long-pressing, or dragging between items
- Reusable circular glass action button

## Package

The reusable component code is under:

```text
app/src/main/java/app/thdev/myapplication/ui/components/liquidglass
```

## Run

```bash
./gradlew :app:installDebug
```

## Notes

This is not an Apple API implementation. It is a Compose approximation of a glass navigation interaction pattern for Android.

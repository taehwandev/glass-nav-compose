# Module Map

## Monorepo Shape

```text
app/                 Android entry point
core/                Android core modules
feature/             Android feature api/impl modules
build-logic/         Android Gradle conventions

apps/
  api/               TypeScript API server
  web/               React/Next.js web app

packages/
  contracts/         canonical URLs, DTOs, fixtures
  api-client/        typed fetch wrapper
```

Android and TypeScript builds are intentionally separate. Share product contracts through URL/API schema and docs, not by making Android consume TypeScript source.

## Ownership

```text
:app
  Android entry point, MainActivity, app theme wiring
  AppRouter, AppDeepLinkResolver, NotmidRouteGraph
  ActivityRoute dispatch

:core:designsystem
  NotmidTheme, color/type/spacing/shape/elevation tokens
  Notmid* Material3 wrappers
  reusable Notmid UI primitives
  Liquid Glass primitives

:core:model
  pure Kotlin immutable models
  no Android, Compose, Color, Dp, resource ids, or repositories

:core:domain
  repository contracts and use cases
  pure Kotlin unless a real Android dependency is unavoidable

:core:data
  fake/static repository implementations
  mapping from product data to domain models

:core:router:api
  pure Kotlin route contracts
  Route, ComposeRoute, ActivityRoute, WebRoute
  DeepLinkSpec, RouteStack, RouteCommand

:core:router:impl
  DefaultRouteRegistry and matching helpers
  no Android Activity launching

:feature:notmid:api
  shared notmid route markers, destination ids, route events

:feature:notmid:common
  product-shaped UI adapters and shared screen sections

:feature:notmid:impl
  notmid app shell and feature orchestration

:feature:*:api
  route contracts, specs, deep-link specs, route events

:feature:*:impl
  Compose screens for that feature only

apps/api
  HTTP API, auth verification boundary, future persistence integrations

apps/web
  React app shell, web routes, shareable detail surfaces

packages/contracts
  shared TypeScript DTOs, canonical web route helpers, deterministic fixtures

packages/api-client
  typed fetch client for web/server-side tooling
```

## Dependency Direction

Allowed:

```text
feature:feed:impl -> feature:feed:api
feature:feed:impl -> feature:notmid:common
feature:notmid:impl -> feature:feed:impl
app -> feature:*:api and impl modules
```

Not allowed:

```text
feature:feed:impl -> feature:map:impl
feature impl -> AppRouter
core:model -> Compose/Android
core:designsystem -> product routes or repositories
core:router:impl -> Android Activity launch
```

## Build Logic

Use project convention plugins:

```text
glassnavlab.android.application
glassnavlab.android.library
glassnavlab.android.library.compose
glassnavlab.kotlin.library
```

Keep conventions small. Do not add Hilt, KSP, Firebase, detekt, or navigation dependencies before code needs them.

## Verification

For module/build changes:

```bash
./gradlew help
./gradlew :app:compileDebugKotlin
```

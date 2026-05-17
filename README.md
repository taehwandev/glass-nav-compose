# notmid

`notmid` is an open-source Android and web reference product for short video place discovery, map context, and place-aware chat.

The product direction is:

```text
not mid. show receipts.
```

This repository is intentionally shaped like a real service instead of a single Android sample. Android, web, server, shared contracts, product docs, and agent-facing project memory live together so URL contracts, API contracts, and feature behavior can evolve in one place.

## Repository Shape

```text
app/                 Android entry point
core/                Android core modules
feature/             Android feature api/impl modules
build-logic/         Android Gradle convention plugins

apps/
  api/               TypeScript API server
  web/               React/Next.js web app

packages/
  contracts/         shared product routes, DTOs, fake fixtures
  api-client/        typed web/server client wrapper

docs/                product and architecture specs
llm-wiki/            short task-oriented project memory for agents
.agents/skills/      repo-local agent skills
```

## Android

The Android app is Jetpack Compose based and keeps reusable UI inside `:core:designsystem`. Feature modules are split into `api` and `impl` where the boundary is useful. Cross-feature communication goes through route/event contracts instead of implementation dependencies.

The Liquid Glass bottom navigation reference still lives in this repository, now under the notmid design system:

```text
core/designsystem/src/main/java/app/thdev/glassnavlab/core/designsystem/component/liquidglass
```

Run Android:

```bash
./gradlew :app:installDebug
```

Verify Android modules:

```bash
./gradlew :app:compileDebugKotlin
./gradlew test
```

Run the full local verification gate:

```bash
bash scripts/verify-local.sh
```

## Web And API

The web/API side is a separate pnpm workspace inside the same git repository. It does not participate in Gradle builds.

Run the API server:

```bash
pnpm install
pnpm api:dev
```

Run the web app:

```bash
pnpm web:dev
```

Local defaults:

```text
API: http://localhost:8787
Web: http://localhost:3000/notmid
```

The web app should open directly into the product shell, not a marketing landing page.

Smoke test web/API locally:

```bash
bash scripts/smoke-web-api.sh
```

## Backend Direction

notmid is server-first:

```text
Android / Web
  -> notmid API Server
      -> Postgres / Redis / Object Storage
      -> Firebase Admin / FCM / App Check when useful
```

Firebase is an auxiliary platform, not the primary product database contract. It can support identity, push notifications, app integrity, crash reporting, analytics, emulator-based tests, or static/web hosting. Production secrets and service account keys must never be committed.

## Deep Links

Web links are product contracts. The same URL should work on web and resolve into an ordered Android route stack.

Examples:

```text
https://thdev.app/notmid
https://thdev.app/notmid/clips/{clipId}
https://thdev.app/notmid/places/{placeId}
https://thdev.app/notmid/profile/settings
```

## Open Source Safety

Never commit:

```text
.env
google-services.json
Firebase Admin SDK JSON
service account JSON
App Check debug tokens
keystores
production API secrets
```

Commit example templates only, such as `.env.example` files with placeholder values.

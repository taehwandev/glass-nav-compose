# Platform Backend

## Direction

notmid is a server-first product with Firebase as auxiliary infrastructure.

Use this mental model:

```text
Android / Web
  -> apps/api
      -> Postgres / Redis / Object Storage later
      -> Firebase Admin / FCM / App Check when useful
```

## Current Scaffold

```text
apps/api
  Hono TypeScript API
  fixture endpoints
  deep-link resolver endpoint

apps/web
  React/Next.js product shell
  /notmid canonical web surface
  detail routes for clips, places, map

packages/contracts
  route helpers
  shared DTOs
  deterministic fixture data

packages/api-client
  typed fetch client
```

## Rules

- Do not put secrets in `packages/contracts`.
- Do not make Android depend on TypeScript packages directly.
- Keep shared behavior as URL/API docs and generated contracts later.
- Web and Android should use the same canonical URL shapes.
- API owns product write policy and token verification.
- Firebase Auth tokens, if used, are verified by the API server.
- FCM/App Check can be added without making Firebase the domain owner.

## Local Defaults

```text
API: http://localhost:8787
Web: http://localhost:3000/notmid
```

## Next Backend Steps

1. Add OpenAPI generation from the API routes.
2. Add `:core:network:api` and `:core:network:impl` on Android.
3. Add `:core:auth:api` and auth state contracts.
4. Decide persistence: Postgres first, then Redis/Object Storage.
5. Add Firebase Admin only for identity/push/app integrity needs.

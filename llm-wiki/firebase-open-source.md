# Firebase And Open Source

## Direction

notmid is server-first. Firebase is useful auxiliary infrastructure, but it should not be the long-term domain contract by default.

Target shape:

```text
Android / Web
  -> apps/api
      -> Postgres / Redis / Object Storage later
      -> Firebase Admin / FCM / App Check when useful
```

Firebase can support:

```text
Authentication
FCM
App Check
Crashlytics / Analytics
Emulator Suite
Hosting or App Hosting for web if useful
```

## Auth Rules

- Signed-out users can browse public feed/map.
- Sign-in is required for capture, upload, save, chat, profile editing, and moderation actions.
- Anonymous browsing should not require production Firebase config.
- Fake/local mode should remain usable without Firebase credentials.
- If Firebase Auth is used, clients send ID tokens to `apps/api`; the API verifies and maps them to notmid users.

## Secret Rules

Never commit:

```text
API keys intended to remain private
service account JSON
production google-services.json
production GoogleService-Info.plist
private Firebase project IDs
MCP API keys
keystores
release signing passwords
```

Allowed:

```text
placeholder config files
documented local config names
emulator config examples
public-safe sample values clearly marked as examples
```

If a real key appears in chat or local files, treat it as exposed and recommend rotating it.

## Suggested Local Files

Use ignored local files for real config:

```text
local.properties
app/google-services.json
apps/api/.env.local
apps/web/.env.local
firebase.local.json
```

Keep examples explicit:

```text
google-services.example.json
.env.example
```

## Implementation Order

1. Keep deterministic fake repositories.
2. Keep API fixture endpoints and web fixture fallback working.
3. Add server auth/session contracts before client SDK wiring.
4. Add Android `:core:network:*` and `:core:auth:*` boundaries.
5. Add Firebase Admin/FCM/App Check only behind `apps/api`.
6. Add production config loading only through ignored local files or secret stores.
7. Add App Check and server write policy before exposing public writes.

## Documentation

Update `docs/specs/firebase-auth-open-source-security.md` when changing auth, Firebase config, emulator setup, or secret-handling policy.

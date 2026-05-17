# Backend, Firebase Auth, And Open Source Security Spec

## Purpose

notmid is evolving into a real short-form, place-based social service with feed, map, capture, inbox/chat, and profile features. The product is server-first, with Firebase used as auxiliary infrastructure where it is the right tool. The repository is open source, so production access must never depend on obscured client files or committed private keys.

This spec defines what can be committed, how login should work, and how server-first modules with Firebase-assisted integrations should be shaped.

## Product Backend Scope

Use Firebase only when fake local data stops being useful and the server boundary needs that integration. The first real backend should cover:

- TypeScript API server for product reads/writes, auth verification, and route/deep-link support.
- Postgres for clip, place, profile, saved item, thread, and message metadata.
- Object storage for short video and thumbnail objects.
- Firebase Authentication as an identity provider if it remains the lowest-friction option.
- Firebase Cloud Messaging for chat and social notifications.
- Firebase App Check for client/app integrity signals.
- Firebase Emulator Suite only for Firebase pieces that need local rules/tests.

Do not add Firebase just to make the sample feel production-like. Add it when a feature needs real identity, push, app integrity, emulator coverage, or another explicit Firebase capability.

## Auth Policy

MVP login should be low-friction but not anonymous-only:

- Browsing feed/map may work signed out or with an anonymous session.
- Capture/upload, chat, save, follow, report, and profile editing require an authenticated user.
- Support anonymous sign-in first, then link to a permanent provider so draft saves and onboarding state are preserved.
- Use Google sign-in as the first permanent provider.
- Avoid password auth in the first implementation unless there is a concrete need. It increases account recovery, abuse, and support surface.
- Keep provider-specific UI inside auth impl. Features consume only app auth state and commands.

Domain-level auth model should stay Firebase-free:

```text
:core:auth:api
  AuthUser
  AuthState
  AuthRepository
  SignInCommand

:core:auth:impl
  FirebaseAuthRepository
  Google credential bridge
  Anonymous-to-Google account linking
```

Features depend on `:core:auth:api`, not Firebase SDKs. If Firebase Auth is used, Android and web clients send Firebase ID tokens to `apps/api`; the API verifies tokens and maps them to notmid users.

## Module Shape

Use `api` and `impl` only where it creates a useful boundary:

```text
apps/api
apps/web
packages/contracts
packages/api-client

:core:auth:api
:core:auth:impl

:core:network:api
:core:network:impl

:core:domain
  FeedRepository
  PlaceRepository
  ChatRepository
  CaptureRepository

:core:data:fake
  Deterministic fake service data for previews/tests

:core:data:firebase
  Firestore/Storage implementations of domain repositories

:feature:feed:api
:feature:feed:impl
:feature:map:api
:feature:map:impl
:feature:capture:api
:feature:capture:impl
:feature:inbox:api
:feature:inbox:impl
:feature:chat:api
:feature:chat:impl
:feature:profile:api
:feature:profile:impl
```

Do not create a `data:api` module if `:core:domain` already owns repository contracts. Do create `api/impl` for auth because auth state and sign-in commands are cross-feature contracts while Firebase is an implementation detail.

## Open Source Secret Rules

Treat these as never-commit files:

- `google-services.json` for real dev/staging/prod projects.
- Firebase Admin SDK JSON files such as `firebase-adminsdk-*.json`.
- Google Cloud service account JSON files.
- App Check debug tokens.
- OAuth client secrets.
- Keystore files, signing passwords, upload keys, and Play Console credentials.
- `.env`, `secrets.properties`, and generated CI credential files.

Allowed in the repository:

- `google-services.example.json` with placeholder values only.
- Documentation showing where local files should be placed.
- Firebase project IDs only if intentionally public.
- Security Rules source files after they contain deny-by-default, reviewed rules.

Firebase Android API keys are not authorization secrets, but they still identify the project. For an open-source repository, prefer not committing real production `google-services.json`; contributors should use their own Firebase project or a local development config.

## Firebase Project Layout

Use separate Firebase projects per environment:

```text
notmid-dev
notmid-staging
notmid-prod
```

Recommended Android application IDs:

```text
app.thdev.notmid.debug
app.thdev.notmid.staging
app.thdev.notmid
```

Each Firebase Android app must register the matching package name and signing certificate fingerprints. Debug and CI fingerprints must never be reused for production.

## Local And CI Configuration

Local development:

- Real `google-services.json` stays untracked.
- `app/google-services.example.json` may document the expected shape.
- Developers create their own Firebase project or request access to a shared dev project.
- App Check debug tokens stay in local environment variables or local secure storage, not files committed to Git.

CI:

- Generate `google-services.json` from encrypted CI secrets at build time.
- Prefer keyless Google Cloud access such as Workload Identity Federation where server-side deploy access is needed.
- If a service account key is unavoidable, store it only in the CI secret store, grant minimal IAM roles, rotate it, and delete it when no longer needed.
- Never print `google-services.json`, service account JSON, App Check debug tokens, or signing credentials in logs.

## API Key Restrictions

For every Firebase API key:

- Apply Android application restrictions by package name and SHA certificate fingerprint.
- Keep API restrictions to only APIs used by the app.
- Set quotas where the Google Cloud API supports quotas.
- Do not reuse a Firebase API key for non-Firebase services such as Maps, Places, or Gemini. Use separate restricted keys for those APIs.

Do not rely on API key restrictions for data access. Firestore and Storage access must be enforced by Firebase Security Rules and App Check.

## App Check

Enable App Check before any public Firebase backend is used:

- Use Play Integrity provider for release and staging builds.
- Use the debug provider only for debug builds and CI.
- Store CI debug tokens in encrypted secrets.
- Do not share debug builds with untrusted users when debug App Check is enabled.
- Turn on enforcement per Firebase product only after staging verifies auth, rules, and upload flows.

## Firestore Data Model Draft

Initial collections:

```text
/users/{uid}
  displayName
  photoUrl
  handle
  bio
  createdAt
  updatedAt

/clips/{clipId}
  ownerUid
  placeId
  caption
  videoPath
  thumbnailPath
  visibility
  likeCount
  commentCount
  createdAt

/places/{placeId}
  name
  region
  category
  geoHash
  lat
  lng
  coverClipId

/users/{uid}/savedClips/{clipId}
/users/{uid}/savedPlaces/{placeId}

/chatThreads/{threadId}
  participantUids: map<uid, true>
  lastMessage
  updatedAt

/chatThreads/{threadId}/messages/{messageId}
  senderUid
  kind
  text
  clipId
  placeId
  createdAt
```

Avoid storing secrets, private tokens, raw device identifiers, or unnecessary location history in Firestore.

## Storage Layout Draft

```text
/clips/{ownerUid}/{clipId}/original.mp4
/clips/{ownerUid}/{clipId}/thumbnail.jpg
/avatars/{uid}/profile.jpg
```

Upload writes must require `request.auth.uid == ownerUid`. Public read is allowed only for published content. Private draft uploads should require owner read access.

## Security Rules Baseline

Rules must start deny-by-default. Minimum policy:

- Users can read public profile fields.
- Users can update only their own profile document.
- Clip creation requires signed-in owner and matching `ownerUid`.
- Clip updates/deletes require owner.
- Public clips are readable by anyone if the product intentionally allows signed-out browsing.
- Chat threads and messages are readable only by participants.
- Message writes require signed-in sender who is a participant.
- Storage clip writes require signed-in owner and validated file path.
- Storage reads follow clip visibility or owner access.

Rules must be tested in the Firebase Emulator Suite before production deployment.

## Chat Policy

Chat is part of the service, but it should be place/clip aware rather than generic messaging:

- `Inbox` lists threads.
- `ChatRoom` supports text, clip attachment, place attachment, and route/share cards.
- Sharing a clip/place emits a feature navigation event from `:feature:*:api`; chat impl resolves it through app navigation, not by depending on another feature impl.
- Chat notifications use FCM after the server-side write path is defined.

## Abuse And Moderation

Auth, API restrictions, and App Check do not solve product abuse. Before public upload:

- Add report/block models.
- Add upload size and duration limits.
- Use server-side validation for publish transitions.
- Consider Cloud Functions for thumbnail generation, video moderation hooks, counter updates, and notification fanout.
- Keep client-written counters non-authoritative.

## Implementation Phases

1. Keep fake repositories and finish product-shaped feature modules.
2. Add `:core:auth:api` and `:core:auth:impl` with anonymous and Google sign-in.
3. Add Firebase Emulator Suite and rules tests.
4. Add `:core:data:firebase` for Firestore read/write metadata.
5. Add Storage upload for capture drafts and published clips.
6. Enable App Check in staging, then production enforcement.
7. Add chat persistence, then FCM notification fanout.
8. Add CI secret generation and secret scanning before public Firebase config work.

## Required Review Gates

Before merging Firebase work:

- No real `google-services.json` or service account JSON in Git.
- No Firebase Admin SDK key in repo history.
- `git diff --check` passes.
- Android compile passes.
- Firestore and Storage rules tests pass in emulator.
- App Check debug provider is excluded from release builds.
- API keys have Android app restrictions and API restrictions.
- Production Firebase project is not used by debug builds.

## References

- Firebase API key guidance: https://firebase.google.com/docs/projects/api-keys
- Firebase Auth anonymous Android flow: https://firebase.google.com/docs/auth/android/anonymous-auth
- Firebase Auth Google sign-in Android flow: https://firebase.google.com/docs/auth/android/google-signin
- Firebase Security Rules basics: https://firebase.google.com/docs/rules/basics
- Firestore Security Rules getting started: https://firebase.google.com/docs/firestore/security/get-started
- Firebase App Check with Play Integrity: https://firebase.google.com/docs/app-check/android/play-integrity-provider
- Firebase App Check debug provider: https://firebase.google.com/docs/app-check/android/debug-provider
- Google Cloud service account key best practices: https://cloud.google.com/iam/docs/best-practices-for-managing-service-account-keys

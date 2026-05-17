# notmid Stitch Project Brief

## Purpose

Use this document as the design-generation brief for Stitch. It is intentionally written as a product brief, not an engineering spec.

## Product Summary

**notmid** is an open-source web and Android app for proving that places are worth visiting with short videos, map context, and place-aware chat.

Tagline:

```text
not mid. show receipts.
```

The app should feel like a real Gen Z local discovery service, not a portfolio sample and not a clean corporate review app. The product promise is simple: every place recommendation needs fresh video proof.

## Audience

- People who decide where to go from short videos instead of static reviews.
- Creators who post cafes, bars, pop-ups, exhibitions, streets, and routes.
- Friends who share clips and turn "pull up?" into a plan.
- Open-source developers studying a real server-backed social app with optional Firebase integrations.

## Core Product Loop

1. Watch a short place clip.
2. See the attached place and freshness signal.
3. Save it, open it on the map, or share it into chat.
4. Record a short receipt clip after visiting.

## First Screen

Do not make a marketing landing page.

The first viewport must be the actual service shell:

- vertical short-video feed as the main surface
- floating Liquid Glass bottom navigation
- place context visible on top of the media
- visible actions for save, chat/share, map/route
- enough real UI density to feel like a working app

## Top-Level Navigation

```text
Feed | Map | Capture | Inbox | Profile
```

Deep-link mental model:

```text
/notmid/feed
/notmid/map
/notmid/capture
/notmid/inbox
/notmid/profile
/notmid/profile/settings
```

## Screen Requirements

### Feed

- Full-height vertical clip surface.
- Attached place card over the media.
- Creator, distance, freshness, save count, and chat/share action.
- Bottom navigation should float over changing video color.
- Signed-out users can browse, signed-in users can save and chat.

### Map

- Full-screen map as the primary surface.
- Clip pins as small circular thumbnails.
- Category rail for Cafe, Night, Exhibit, Walk, Pop-up.
- Bottom place preview sheet after selecting a pin.
- Show live/freshness signal, not only star ratings.

### Capture

- Camera/upload surface.
- Glass composer panel for caption, mood tags, place attach, visibility, and publish.
- Keep publish visible but not oversized.
- Include upload/draft state.

### Inbox

- Thread list and selected chat state.
- Conversations can include text, clip cards, place cards, and route planning.
- It should feel place-aware, not like a generic messenger.

### Profile

- Creator identity, handle, avatar, posted receipts, saved places, and settings entry.
- Settings should be reachable as a nested route under profile.
- Show auth state and open-source/Firebase safety as product settings, not marketing copy.

## Visual Direction

Mood:

- slightly chaotic but controlled
- fresh, local, mobile-native
- video-first and map-aware
- confident, not luxury
- "online" enough to justify the name `notmid`

Use:

- Liquid Glass style floating navigation and panels
- sharp video/place thumbnails
- high-contrast dark text on warm off-white app chrome
- vivid accents pulled from clip/media content
- compact cards with 8-24dp radius depending on hierarchy
- real product UI density

Avoid:

- generic SaaS dashboards
- beige lifestyle branding
- one-note purple gradients
- oversized hero copy
- stock-photo blur backgrounds
- empty marketing sections
- cards nested inside cards
- fake decorative orbs or blobs

## Design System Tokens

Use the same tokens as the Android `:core:designsystem` module.

Color:

```text
Ink #101114
Slate #2D333B
Mist #F4F6F8
Warm Mist #F3F1EC
Cloud #FFFFFF
Signal Green #14B87A
Route Blue #2F6BFF
Warm Clip #FF704D
Night Violet #7A4EF3
Alert Red #F04452
Light Glass rgba(255,255,255,0.68)
Dark Glass rgba(18,20,24,0.56)
Glass Stroke rgba(255,255,255,0.35)
```

Typography:

```text
Display 34/38 700
Title 22/28 700
Headline 18/20 600
Body 15/22 400
Label 12/16 600
Caption 11/14 500
```

Shape:

```text
Controls 8
Cards 16
Floating glass cards 24
Sheets 28
Pills 999
```

## Component Inventory

- Vertical clip player
- Place receipt card
- Clip action rail
- Liquid Glass bottom navigation
- Glass metric tile
- Pill filter button
- Gradient summary card
- Gradient place card
- Map pin with video thumbnail
- Place preview sheet
- Capture composer
- Chat thread row
- Message bubble with clip/place attachment
- Profile header
- Settings list
- Auth gate state
- Empty and loading states

## Backend/Firebase/Open Source Constraints

The design can show server-backed behavior and optional Firebase-assisted auth/push/app integrity, but must not imply committed secrets.

- Login is needed for capture, chat, save, and profile edit.
- Browsing can work signed out.
- Use copy such as "Bring your own Firebase project" only in settings/onboarding developer surfaces.
- Do not show real API keys, service accounts, private tokens, or production project IDs.

## Stitch Prompt

```text
Design a responsive product web app for "notmid", an open-source server-backed short-video place discovery service with Firebase-assisted auth, push, and app integrity. The tagline is "not mid. show receipts." The first screen must be the actual app shell, not a marketing landing page.

Create a mobile-first UI with a vertical short-video Feed, a full-screen Map with clip thumbnail pins, a Capture composer, an Inbox for place-aware chat, and a Profile with settings. Use top-level navigation: Feed, Map, Capture, Inbox, Profile. Include nested Profile > Settings.

The product is for Gen Z local discovery: cafes, bars, pop-ups, exhibits, walks, nightlife, and routes. Every place recommendation should feel backed by fresh video proof. The app should feel slightly chaotic but controlled, real, social, and video-first.

Use Liquid Glass style floating bottom navigation and translucent panels over video/map surfaces. Keep the UI dense enough to feel usable. Use vivid media-derived accents, warm off-white app chrome, high-contrast text, compact cards, clip action rails, place receipt cards, map preview sheets, chat attachments, and auth-gated capture/save/chat states.

Avoid generic SaaS dashboards, beige lifestyle branding, one-note purple gradients, stock blur backgrounds, oversized hero sections, nested cards, and marketing copy. Do not show real Firebase keys or secrets.
```

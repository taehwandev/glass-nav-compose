# notmid Product And Design Concept

## Service Name

Preferred name: **notmid**

Meaning:

- **Mid**: overhyped, average, not worth the trip.
- **notmid**: the place passed the vibe proof.
- Clips are receipts, the map shows what is live, and chat turns "pull up?" into a plan.

Working tagline:

> not mid. show receipts.

Longer positioning:

> notmid is an open-source, server-backed web and Android app where people prove places are worth pulling up to with short videos, map context, and place-aware chat.

Name status:

- This is a working product name, not a legal trademark clearance.
- It intentionally feels a little too online. That is the point: it should feel like a real social product, not a clean portfolio sample.
- It avoids the most obvious already-used nearby candidates checked during ideation, including LoopSpot, SpotLoop, PlaceLoop, GlassTrail, Spotverse, Pinloop, PlacePulse, MomentMap, SceneDrop, MapReel, WYA, Pullup, VibeCheck, YapMap, and HypeMap.
- Before publishing under this name, check domain, package name, GitHub org/repo name, app store listings, and trademark availability.

Backup names if `notmid` cannot be used:

```text
no mid
midless
receipts
pullupcam
rnmap
```

Avoid names that sound too polished:

```text
Reelocal
GlassTrail
PlacePulse
LoopSpot
Spotverse
```

## Product Thesis

This should not feel like a component sample or a TikTok clone. The product is a real service where location, short video, and conversation are equally important.

Core idea:

> People do not only want reviews. They want to see what a place feels like right now, then save it, share it, and talk about going there.

Primary use cases:

- Discover nearby cafes, bars, exhibits, pop-ups, streets, and weekend routes through short vertical clips.
- Open a map to see where current clips are happening.
- Attach a place to every clip so content is grounded in the real world.
- Share a place or clip into chat and plan with friends.
- Save clips, places, and routes.
- Let contributors run their own local backend and optional Firebase project because the app is open source.

## Audience

Primary:

- People who decide where to go from short videos instead of static reviews.
- Creators who record local places, routes, pop-ups, cafes, exhibitions, and nightlife.
- Open-source Android/web developers who want a real server-backed social app reference.

Secondary:

- Small teams studying product-shaped Android, web, API, and Firebase-adjacent architecture.
- Designers exploring glass UI over video/map surfaces.

## Platform Direction

Build both:

- **Web app**: open-source first surface, shareable links, desktop exploration, responsive mobile web.
- **Android app**: camera, upload, map, chat, and Liquid Glass navigation reference.

The web app should not be a marketing site. First viewport should be the actual service shell with usable feed/map/inbox surfaces.

## Backend And Firebase Direction

Primary backend:

- TypeScript API server for product data, auth verification, write policy, and future persistence.
- Postgres for product metadata when fake/local mode is no longer enough.
- Object storage for short videos, thumbnails, and avatars.
- Redis or another realtime/cache layer when chat/feed freshness needs it.

Firebase products can support:

- Firebase Authentication: anonymous browsing, Google sign-in for upload/chat/save.
- Firebase Cloud Messaging: chat and social notifications.
- Firebase App Check: web and Android abuse reduction.
- Firebase Hosting or App Hosting: web deployment if it fits the final web stack.
- Firebase Emulator Suite: open-source local development.

Open-source rule:

- No production Firebase config or service account key is required to run local fake mode.
- Contributors can bring their own Firebase project.
- Real production keys/configs are not committed.
- Example config files are placeholders only.

## Information Architecture

Top navigation:

```text
Feed | Map | Capture | Inbox | Profile
```

Secondary surfaces:

```text
Feed
  Clip Detail
  Share Sheet
  Creator Profile

Map
  Place Preview
  Place Detail
  City/Category Result

Capture
  Upload Draft
  Place Attach
  Publish Review

Inbox
  Thread List
  Chat Room
  Place/Clip Attachment

Profile
  Saved Clips
  Saved Places
  My Clips
  Settings
```

Deep links:

```text
https://thdev.app/notmid/feed
https://thdev.app/notmid/clip/{clipId}
https://thdev.app/notmid/map/{city}/{category}
https://thdev.app/notmid/place/{placeId}
https://thdev.app/notmid/capture/place/{placeId}
https://thdev.app/notmid/inbox
https://thdev.app/notmid/chat/{threadId}
https://thdev.app/notmid/profile/{handle}
```

## Core Screens

### Feed

Purpose:

- Consume short vertical clips quickly.
- See place context without leaving the video.

Layout:

- Full-height vertical video surface.
- Floating glass place card near the lower third.
- Right-side compact actions: like, save, chat/share, route.
- Bottom glass navigation.
- Swipe or scroll between clips.

Key states:

- Signed out: can browse public clips.
- Signed in: can save, comment, share to chat.
- Empty location permission: use trending feed.

### Map

Purpose:

- Discover places spatially.
- Show where clips are active now.

Layout:

- Full-screen map.
- Clip pins as small circular thumbnails.
- Glass category rail: Cafe, Work, Night, Exhibit, Walk.
- Bottom place preview sheet when a pin is selected.

Key states:

- No location permission.
- City/category route loaded from URL.
- Clustered pins at low zoom.

### Capture

Purpose:

- Record or upload a short clip and attach a place.

Layout:

- Camera/upload surface.
- Glass composer panel with caption, mood tags, place attach, visibility.
- Publish action is prominent but not oversized.

Key states:

- Requires sign-in.
- Draft saved locally before upload.
- Upload progress and retry.

### Inbox

Purpose:

- Make chat useful for place planning, not generic messaging.

Layout:

- Thread list with clip/place preview thumbnails.
- Unread count and participant avatars.
- Search/filter for people and shared places.

### Chat Room

Purpose:

- Talk around clips, places, and routes.

Layout:

- Messages grouped by time.
- Place cards and clip cards as rich message bubbles.
- Floating glass input composer.
- Attachment actions: clip, place, route.

### Profile

Purpose:

- Creator identity and saved content.

Layout:

- Profile header with avatar, handle, short bio.
- Tabs: Clips, Saved, Places, Routes.
- Settings entry for account and privacy.

## Visual Design Concept

Design name:

> Urban Glass Social

Principles:

- Video and map are the canvas. UI floats over them.
- Glass is functional, not decorative. Use it for navigation, place cards, controls, composers, and sheets.
- Keep high-density app surfaces calm. Avoid marketing hero composition.
- Rounded corners should stay restrained: cards 8-16px, sheets 20-28px only when they float over media.
- Use real photo/video thumbnails as the main visual energy.
- Avoid purple-blue gradient dominance. Use diverse media-driven color adaptation instead.

Mood:

- Urban, social, fast, map-aware, slightly cinematic.
- More “live city surface” than “creator platform dashboard”.
- Clear enough for open-source contributors to understand and extend.

## Color System

Base:

```text
Ink:        #101114
Slate:      #2D333B
Mist:       #F4F6F8
Cloud:      #FFFFFF
Line:       rgba(16,17,20,0.10)
```

Accent set:

```text
Signal Green: #14B87A
Route Blue:   #2F6BFF
Warm Clip:    #FF704D
Night Violet: #7A4EF3
Alert Red:    #F04452
```

Glass surfaces:

```text
Light glass: rgba(255,255,255,0.68)
Dark glass:  rgba(18,20,24,0.56)
Stroke:      rgba(255,255,255,0.35)
Shadow:      rgba(0,0,0,0.18)
Blur:        18-28px
```

Usage:

- Feed controls adapt between light/dark glass based on video luminance.
- Map controls use light glass by default.
- Chat composer uses light glass on light backgrounds and dark glass over media attachments.
- Accent colors are stateful, not background themes.

## Typography

Use a clean sans-serif stack:

```text
Inter, SF Pro, Roboto, system-ui, sans-serif
```

Scale:

```text
Display: 32/38, weight 700
Title:   22/28, weight 700
Body:    15/22, weight 400
Label:   12/16, weight 600
Caption: 11/14, weight 500
```

Do not use viewport-based font sizing. Keep text inside cards concise.

## Component Direction

Core components:

- `GlassBottomNav`
- `GlassTopBar`
- `ClipPlayer`
- `PlaceCard`
- `ClipActionRail`
- `MapPinCluster`
- `PlacePreviewSheet`
- `CaptureComposer`
- `ChatComposer`
- `RichMessageBubble`
- `AuthGate`

States every component should consider:

- Loading.
- Empty.
- Signed out.
- Permission denied.
- Offline.
- Uploading.
- Error with retry.

## Web App Layout

Desktop:

- Three-column service shell for logged-in or demo mode:
  - Left rail: navigation and open-source project identity.
  - Center: active feed/map/chat content.
  - Right rail: selected place, thread, or creator context.
- Feed video remains vertical and centered.
- Map fills the content region.

Mobile web:

- Bottom navigation.
- Full-screen feed/map/capture surfaces.
- Sheets and overlays instead of sidebars.

Open-source visible affordance:

- Small “Open source” link in side rail/profile/settings.
- Do not make the main screen a GitHub marketing page.

## Logo Direction

Wordmark:

- `notmid`
- Always lowercase in product UI.
- The mark can combine a map pin and a play loop.

Icon idea:

- Rounded map pin outline.
- Inner play triangle or vertical reel frame.
- One small orbit/loop stroke to imply short looping clips.

Avoid:

- Generic camera-only icon.
- TikTok-like note mark.
- Overly detailed map illustration.

## Stitch Prompt

Use this prompt to generate the first design direction:

```text
Design a production-quality responsive web app called notmid.

notmid is an open-source, server-backed location-based short video service. It lets people discover real places through vertical clips, map pins, and place-aware chat. The app is not a marketing landing page and not a component demo. The first screen must feel like the real product.

Create a Gen Z, slightly cringe, internet-native "Urban Glass Social" interface:
- Full app shell, not a hero page.
- Desktop layout with a left navigation rail, central active content, and right contextual panel.
- Mobile layout with full-screen content and bottom navigation.
- Use glassmorphism only for functional surfaces: navigation, place cards, action controls, chat composer, map filters, and bottom sheets.
- Use real-looking vertical video thumbnails and place imagery as the visual energy.
- Avoid decorative gradient blobs, oversized marketing cards, and purple/blue-dominant gradient backgrounds.
- Keep cards restrained with 8-16px radius; floating sheets can use 20-28px radius.
- The UI can say things like "not mid", "show receipts", "pull up?", "worth it", "live rn", and "passed the vibe". Keep it fun but not childish.

Primary screens to represent:
1. Feed: vertical video in the center, floating glass place card, right-side action rail, bottom/side navigation.
2. Map: full map surface with video thumbnail pins, glass category filter rail, selected place preview sheet.
3. Capture: upload/camera surface with glass composer for caption, place, mood tags, visibility, and publish.
4. Inbox/Chat: thread list and chat room with clip/place rich message bubbles and a floating glass input composer.
5. Profile: creator profile, clips, saved places, and open-source project link.

Navigation labels:
Feed, Map, Capture, Inbox, Profile.

Brand:
- Name: notmid
- Tagline: not mid. show receipts.
- Tone: Gen Z, urban, social, fast, map-aware, slightly cinematic, a little unserious, still practical for open-source contributors.

Color system:
- Ink #101114
- Slate #2D333B
- Mist #F4F6F8
- Cloud #FFFFFF
- Signal Green #14B87A
- Route Blue #2F6BFF
- Warm Clip #FF704D
- Night Violet #7A4EF3
- Alert Red #F04452
- Light glass rgba(255,255,255,0.68)
- Dark glass rgba(18,20,24,0.56)

Typography:
Use Inter or a similar modern sans-serif. Keep text compact and readable. Do not scale fonts based on viewport width.

Important UX details:
- Signed out users can browse feed/map.
- Capture, save, chat, and profile editing require sign-in.
- Include a subtle backend/Firebase/open-source developer affordance in settings or side rail, but do not let it dominate the consumer app.
- Include realistic empty/loading/permission-denied states where helpful.

Deliver polished high-fidelity screens for desktop and mobile.
```

## Implementation Notes

Suggested web stack:

```text
Next.js React
TypeScript
Hono API server
Shared route/API contracts
Firebase Auth
Firebase App Check
Firebase Hosting or App Hosting
```

Suggested repo shape:

```text
apps/
  web/
  api/
packages/
  contracts/
  api-client/
docs/
```

For open-source mode:

- Provide fake data mode by default.
- API fixture mode works by default.
- Firebase mode activates only when local config exists.
- Never require production Firebase credentials to run the app locally.

## References Checked During Naming

- LoopSpot: https://alternativeto.net/software/loopspot/about/
- SpotLoop: https://parkwithspotloop.com/
- PlaceLoop: https://placeloop.com/
- GlassTrail: https://www.exafol.com/products/glasstrail
- Spotverse: https://spotverse.co/
- Pinloop: https://play.google.com/store/apps/details?id=com.prokta.pinloop
- PlacePulse: https://www.placepulse.app/
- MomentMap: https://momentmap.de/
- SceneDrop: https://www.scenedrop.com/
- MapReel: https://mapreel.app/help

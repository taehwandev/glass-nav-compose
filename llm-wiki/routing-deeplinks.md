# Routing And Deep Links

## Direction

notmid uses typed route contracts, route events, and a registry. Avoid ad-hoc string navigation.

Feature API modules own route contracts. App shell assembles and executes route stacks.

## Core Types

```text
Route
ComposeRoute
ActivityRoute
WebRoute
RouteSpec
StaticRouteSpec
TopLevelRouteSpec
ActivityRouteSpec
DeepLinkSpec
RouteStack
RouteCommand
RouteEvent
Router
WebRouteLink
```

## Route Targets

Compose routes:

```text
FeedRoute
MapRoute
CaptureRoute
InboxRoute
ProfileRoute
ClipDetailRoute(clipId)
PlaceDetailRoute(placeId)
ChatThreadRoute(threadId)
ProfileSettingsRoute
```

Activity route:

```text
WebViewRoute(url, title, mode)
```

WebView is intentionally an Activity because lifecycle, reload behavior, file chooser, permissions, history, and fullscreen media are cleaner there.

## Deep-Link Behavior

Deep links resolve to ordered stacks.

```text
https://thdev.app/notmid
  -> [Feed]

https://thdev.app/notmid/feed
  -> [Feed]

https://thdev.app/notmid/profile/settings
  -> [Profile, ProfileSettings]

https://thdev.app/notmid/feed/clips/{clipId}
https://thdev.app/notmid/clips/{clipId}
  -> [Feed, ClipDetail(clipId)]

https://thdev.app/notmid/map/places/{placeId}
https://thdev.app/notmid/places/{placeId}
  -> [Map, PlaceDetail(placeId)]

https://thdev.app/notmid/inbox/chats/{threadId}
https://thdev.app/notmid/chats/{threadId}
  -> [Inbox, ChatThread(threadId)]

https://thdev.app/notmid/web?url={encodedUrl}
  -> [WebViewRoute(url)]
```

WebView URL accepts only `http` and `https`.

## Adding A Dynamic Screen

1. Add route data class in the owning `feature:*:api`.
2. Add `RouteSpec`.
3. Add `DeepLinkSpec`.
4. Add feature route event if UI opens it.
5. Register spec in `NotmidRouteGraph`.
6. Handle event in `AppRouter`.
7. Render route in `NotmidShellScreen` or owning shell.
8. Add tests:
   - `AppDeepLinkResolverTest`
   - `AppRouterTest`

## Boundaries

Allowed:

```text
feature impl emits FeatureRouteEvent
app converts event to RouteStack
app launches ActivityRoute
```

Not allowed:

```text
feature impl depends on another feature impl
feature impl parses app web URLs
feature impl starts another feature Activity directly
```

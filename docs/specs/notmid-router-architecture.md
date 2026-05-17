# notmid Router Architecture

## Direction

notmid should use a production-shaped router, not ad-hoc string navigation.

The design follows the useful part of FirFin's router idea:

- feature API modules own their journey/route contracts
- app-level router gathers those contracts
- deep links resolve through registered specs
- navigation is testable without feature implementation modules

The implementation is adapted for notmid's current shape:

- single Android Activity
- Compose screen stack today
- Activity launch support later
- web links must produce ordered route stacks

## Modules

```text
:core:router:api
  Route
  ComposeRoute
  ActivityRoute
  WebRoute
  RouteSpec
  ActivityRouteSpec
  TopLevelRouteSpec
  DeepLinkSpec
  RouteStack
  RouteCommand
  Router
  RouteEvent
  RouteRegistry
  WebRouteLink

:core:router:impl
  DefaultRouteRegistry
  StaticRouteDeepLinkSpec
  PrefixRouteDeepLinkSpec

:feature:*:api
  FeatureRoute
  FeatureRouteSpec
  FeatureDeepLinkSpec
  FeatureRouteEvent when needed

:feature:webview:api
  WebViewRoute
  WebViewRouteSpec
  WebViewDeepLinkSpec

:feature:*:impl
  Compose screens only

:feature:webview:impl
  WebViewActivity
  WebView intent factory

:app
  AppRouter
  AppDeepLinkResolver
  NotmidRouteGraph
  auth gate
  Activity dispatch for ActivityRoute
```

## Rule

Feature implementation modules must not navigate directly to another feature implementation.

Allowed:

```text
feature:feed:impl -> emits OpenPlace(placeId)
feature:feed:api -> owns Feed route/event contracts
app -> converts OpenPlace(placeId) into [Map, PlaceDetail(placeId)]
```

Not allowed:

```text
feature:feed:impl -> depends on feature:map:impl
feature:feed:impl -> constructs Android Intent for another feature
feature:feed:impl -> parses app web URLs
```

## Route Targets

Most app screens are `ComposeRoute`.

```text
FeedRoute
MapRoute
CaptureRoute
InboxRoute
ProfileRoute
ClipDetailRoute(clipId)
PlaceDetailRoute(placeId)
ChatThreadRoute(threadId)
```

WebView is intentionally an `ActivityRoute` because Android WebView lifecycle, reload behavior, file chooser, permission, history, and fullscreen media are usually cleaner in a dedicated Activity.

```text
WebViewRoute(url, title, mode)
  -> WebViewActivity
```

## Route Ownership

Each feature API owns its contract:

```kotlin
object FeedRoute : NotmidTopLevelRoute
object FeedRouteSpec : NotmidTopLevelRouteSpec<FeedRoute>
object FeedDeepLinkSpec : NotmidStaticDeepLinkSpec(FeedRoute)
```

Dynamic routes should follow the same shape:

```kotlin
data class ClipDetailRoute(
    val clipId: String,
) : NotmidRoute

data class PlaceDetailRoute(
    val placeId: String,
) : NotmidRoute

object PlaceDetailRouteSpec : NotmidRouteSpec<PlaceDetailRoute>
object PlaceDeepLinkSpec : DeepLinkSpec
```

Activity routes follow the same contract style:

```kotlin
data class WebViewRoute(
    val url: String,
) : ActivityRoute

object WebViewRouteSpec : ActivityRouteSpec<WebViewRoute>
object WebViewDeepLinkSpec : DeepLinkSpec
```

## Deep Link Behavior

Deep links must resolve to ordered stacks, not just a destination.

```text
https://thdev.app/notmid
  -> [Feed]

https://thdev.app/notmid/feed
  -> [Feed]

https://thdev.app/notmid/profile/settings
  -> [Profile, ProfileSettings]

https://thdev.app/notmid/feed/clips/{clipId}
  -> [Feed, ClipDetail(clipId)]

https://thdev.app/notmid/map/places/{placeId}
  -> [Map, PlaceDetail(placeId)]

https://thdev.app/notmid/inbox/chats/{threadId}
  -> [Inbox, ChatThread(threadId)]

https://thdev.app/notmid/web?url={encodedUrl}
  -> [WebViewRoute(url)]
```

The parser also accepts the shorter canonical object paths:

```text
https://thdev.app/notmid/clips/{clipId}
https://thdev.app/notmid/places/{placeId}
https://thdev.app/notmid/chats/{threadId}
```

Those still resolve through the owning top-level route first, so browser links and in-app events share the same stack shape.

## Why Registry

Without a registry, app routing becomes a growing chain of string checks:

```text
if segment == "feed"
if segment == "map"
if segment == "profile" && child == "settings"
```

With a registry:

- each feature adds a route spec and deep link spec
- app adds the spec to one registry list
- tests assert stack output
- feature implementation stays independent

## Activity Support

When a feature needs a separate Activity, do not replace the route system.

Use `ActivityRoute`:

```kotlin
interface ActivityRoute {
    val activityKey: String
}
```

Then app-level dispatch decides:

```text
RouteCommand([Capture])
  -> Compose destination today
  -> Activity launcher tomorrow if Capture becomes a separate Activity

RouteCommand([WebViewRoute])
  -> WebViewActivity launch
```

The feature API remains stable either way.

## Testing Strategy

Minimum tests:

- deep link string resolves to expected `RouteStack`
- unknown deep link returns null
- route event resolves to expected stack
- default app route is Feed
- dynamic route arguments are parsed and preserved
- activity route commands queue an Activity launch request without replacing Compose stack

Current tests:

- `AppDeepLinkResolverTest`
- `AppRouterTest`

package app.thdev.glassnavlab.feature.capture.api

import app.thdev.glassnavlab.feature.notmid.api.NotmidDestinationIds
import app.thdev.glassnavlab.feature.notmid.api.NotmidStaticDeepLinkSpec
import app.thdev.glassnavlab.feature.notmid.api.NotmidTopLevelRouteSpec
import app.thdev.glassnavlab.feature.notmid.api.NotmidTopLevelRoute

object CaptureRoute : NotmidTopLevelRoute {
    override val route: String = "notmid/capture"
    override val selectedDestinationId: String = NotmidDestinationIds.CAPTURE
    override val title: String = "Capture"
    override val webPathSegments: List<String> = listOf(NotmidDestinationIds.CAPTURE)
}

object CaptureRouteSpec : NotmidTopLevelRouteSpec<CaptureRoute> {
    override val route: CaptureRoute = CaptureRoute
}

object CaptureDeepLinkSpec : NotmidStaticDeepLinkSpec(CaptureRoute)

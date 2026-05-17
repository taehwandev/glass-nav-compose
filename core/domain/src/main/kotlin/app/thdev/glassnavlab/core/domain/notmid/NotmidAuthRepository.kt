package app.thdev.glassnavlab.core.domain.notmid

import app.thdev.glassnavlab.core.model.notmid.NotmidAuthState

interface NotmidAuthRepository {
    fun authState(): NotmidAuthState

    fun signInLocal(): NotmidAuthState
}

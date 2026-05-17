package app.thdev.glassnavlab.core.model.notmid

enum class NotmidAuthMode {
    Fake,
    Firebase,
    Disabled,
}

enum class NotmidAuthProvider {
    Fake,
    Anonymous,
    Google,
}

enum class NotmidAuthRequiredAction {
    Capture,
    Save,
    Chat,
    ProfileEdit,
    Moderation,
}

data class NotmidAuthUser(
    val id: String,
    val handle: String,
    val displayName: String,
    val homeNeighborhood: String,
    val avatarImageUrl: String,
    val roles: List<String>,
)

data class NotmidAuthSession(
    val accessToken: String,
    val provider: NotmidAuthProvider,
    val expiresAt: String,
    val user: NotmidAuthUser,
)

data class NotmidAuthState(
    val mode: NotmidAuthMode,
    val session: NotmidAuthSession?,
    val requiredActions: List<NotmidAuthRequiredAction>,
) {
    val isAuthenticated: Boolean
        get() = session != null
}

package app.thdev.glassnavlab.core.domain.notmid

class GetNotmidAuthStateUseCase(
    private val repository: NotmidAuthRepository,
) {
    operator fun invoke() = repository.authState()
}

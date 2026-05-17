package app.thdev.glassnavlab.core.domain.notmid

class SignInToNotmidUseCase(
    private val repository: NotmidAuthRepository,
) {
    operator fun invoke() = repository.signInLocal()
}

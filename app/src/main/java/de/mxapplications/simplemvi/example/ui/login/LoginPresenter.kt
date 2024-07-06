package de.mxapplications.simplemvi.example.ui.login

import de.mxapplications.simplemvi.Action
import de.mxapplications.simplemvi.Presenter
import de.mxapplications.simplemvi.State
import de.mxapplications.simplemvi.example.repository.RedditRepository
import kotlinx.coroutines.flow.map

class LoginPresenter(
    private val repository: RedditRepository
): Presenter<LoginState, LoginAction>() {
    override fun startingState(): LoginState = LoginState()

    override fun process(action: LoginAction) {
        when (action) {
            is LoginAction.StartLogin -> collect(repository.loginUrl().map { url ->
                LoginAction.LoginUrlReceived(url)
            })
            is LoginAction.CheckUrl -> collect(repository.checkUrl(action.url).map {
                LoginAction.LoginComplete(it)
            })
        }
    }

    override fun reduce(lastState: LoginState, action: LoginAction): LoginState {
        return when (action) {
            is LoginAction.LoginUrlReceived -> lastState.copy(
                loginUrl = action.url
            )
            is LoginAction.LoginComplete -> lastState.copy(
                loginSuccess = action.error == null
            )
            // Actions like StartLogin and CheckUrl only trigger behavior (see process()) but don't require UI updates. Therefore, the state remains unchanged.
            else -> lastState
        }
    }
}

data class LoginState(
    val loginUrl: String? = null,
    val loginSuccess: Boolean = false
): State

sealed class LoginAction: Action {
    object StartLogin: LoginAction()
    data class LoginUrlReceived(val url: String): LoginAction()
    data class CheckUrl(val url: String): LoginAction()
    data class LoginComplete(val error: Throwable?): LoginAction()
}
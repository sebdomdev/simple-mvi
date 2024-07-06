package de.mxapplications.simplemvi.example.ui.post

import de.mxapplications.simplemvi.Action
import de.mxapplications.simplemvi.Presenter
import de.mxapplications.simplemvi.State
import de.mxapplications.simplemvi.example.model.Post
import de.mxapplications.simplemvi.example.repository.RedditRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * postId is used to query the repository for a specific post. postId should be set before registering a [de.mxapplications.simplemvi.Renderer] with the [Presenter]. Otherwise it won't be available in [source] to query the repository.
 *
 */
class PostPresenter(
    private val repository: RedditRepository
): Presenter<PostState, PostAction>() {
    lateinit var postId: String

    override fun startingState(): PostState {
        return PostState()
    }

    override fun source(): Flow<PostAction> {
        // Query the repository for a specific post, using the postId
        return repository.loadPost(postId).map {
            PostAction.PostLoaded(it)
        }
    }

    override fun reduce(lastState: PostState, action: PostAction): PostState {
        return when (action) {
            is PostAction.PostLoaded -> lastState.copy(
                loading = false,
                post = action.post
            )
        }
    }
}

data class PostState(
    val loading: Boolean = true,
    val post: Post? = null
) : State

sealed class PostAction: Action {
    data class PostLoaded(val post: Post): PostAction()
}
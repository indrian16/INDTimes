package io.indrian16.indtimes.ui.detail

import io.indrian16.indtimes.data.model.Article

sealed class DetailState {

    abstract val data: Article
}

data class DefaultState(override val data: Article): DetailState()
data class ChangeIconState(override val data: Article, val isBookmark: Boolean): DetailState()
data class ErrorState(override val data: Article, val errorMessage: String): DetailState()
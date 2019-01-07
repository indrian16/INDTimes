package io.indrian16.indtimes.ui.detail

import io.indrian16.indtimes.data.model.Article

sealed class DetailState {

    abstract val data: Article
}

data class DefaultDetailState(override val data: Article): DetailState()
data class ChangeIconDetailState(override val data: Article, val isExist: Boolean): DetailState()
data class ErrorDetailState(override val data: Article, val errorMessage: String): DetailState()
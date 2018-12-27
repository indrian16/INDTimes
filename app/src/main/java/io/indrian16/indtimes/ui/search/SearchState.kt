package io.indrian16.indtimes.ui.search

import io.indrian16.indtimes.data.model.Article

sealed class SearchState {

    abstract val dataList: List<Article>
}

data class DefaultSearchState(override val dataList: List<Article>): SearchState()
data class LoadingSearchState(override val dataList: List<Article>): SearchState()
data class ErrorSearchState(override val dataList: List<Article>, val errorMessage: String): SearchState()
data class NotFoundSearchState(override val dataList: List<Article>): SearchState()
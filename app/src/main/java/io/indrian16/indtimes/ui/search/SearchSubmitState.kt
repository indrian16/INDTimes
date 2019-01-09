package io.indrian16.indtimes.ui.search

import io.indrian16.indtimes.data.model.Article

sealed class SearchSubmitState

data class GetDataState(val dataList: List<Article>): SearchSubmitState()
data class ErrorState(val errorMessage: String) : SearchSubmitState()
object LoadingState : SearchSubmitState()
object EmptyState : SearchSubmitState()
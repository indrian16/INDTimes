package io.indrian16.indtimes.ui.news

import io.indrian16.indtimes.data.model.Article

sealed class NewsState {

    abstract val dataList: List<Article>
}
data class NewsDefaultState(override val dataList: List<Article>): NewsState()
data class NewsLoadingState(override val dataList: List<Article>): NewsState()
data class NewsEmptyListState(override val dataList: List<Article>): NewsState()
data class NewsErrorState(val errorMessage: String, override val dataList: List<Article>): NewsState()
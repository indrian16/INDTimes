package io.indrian16.indtimes.ui.favorite

import io.indrian16.indtimes.data.model.Favorite

sealed class FavoriteListState {

    abstract val dataList: List<Favorite>
}
data class DefaultState(override val dataList: List<Favorite>): FavoriteListState()
data class LoadingState(override val dataList: List<Favorite>): FavoriteListState()
data class EmptyState(override val dataList: List<Favorite>): FavoriteListState()
data class ErrorState(override val dataList: List<Favorite>, val errorMessage: String): FavoriteListState()
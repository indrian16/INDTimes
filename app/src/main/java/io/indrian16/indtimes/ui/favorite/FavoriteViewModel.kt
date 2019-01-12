package io.indrian16.indtimes.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.data.model.Favorite
import io.indrian16.indtimes.data.repository.LocalRepository
import io.indrian16.indtimes.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val localRepository: LocalRepository) : ViewModel() {

    val favoriteListStateLiveData = MutableLiveData<FavoriteListState>()

    private val compositeDisposable = CompositeDisposable()

    fun updateFavorite() {

        getFavoriteList()
    }

    private fun getFavoriteList() {

        compositeDisposable += localRepository.getFavoriteList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onLoading() }
                .subscribe(this::onReceivedList, this::onError)
    }

    private fun onReceivedList(dataList: List<Favorite>) {

        if (dataList.isNotEmpty()) {

            favoriteListStateLiveData.value = DefaultState(dataList)

        } else {

            favoriteListStateLiveData.value = EmptyState(emptyList())
        }
    }

    private fun onLoading() {

        favoriteListStateLiveData.value = LoadingState(emptyList())
    }

    private fun onError(throwable: Throwable) {

        d { "${throwable.message}" }
        favoriteListStateLiveData.value = ErrorState(emptyList(), throwable.message.toString())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}
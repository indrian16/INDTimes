package io.indrian16.indtimes.ui.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.repository.Repository
import io.indrian16.indtimes.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val stateLiveData = MutableLiveData<SearchState>()
    private val compositeDisposable = CompositeDisposable()

    private fun obtainCurrentData() = stateLiveData.value?.dataList ?: emptyList()

    init {

        stateLiveData.value = NotFoundSearchState(emptyList())
    }

    fun getNewsListOnChange(query: String) {

        if (query == "") {

            stateLiveData.value = NotFoundSearchState(emptyList())

        } else {

            compositeDisposable += repository.getEverything(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onLoading() }
                .subscribe(this::onReceivedList, this::onError)
        }
    }

    private fun onLoading() {

        stateLiveData.value = LoadingSearchState(obtainCurrentData())
    }

    private fun onReceivedList(dataList: List<Article>) {

        if (dataList.isNotEmpty()) {

            val currentData = dataList.toMutableList()
            currentData.addAll(dataList)

            stateLiveData.value = DefaultSearchState(currentData)

        } else {

            stateLiveData.value = NotFoundSearchState(emptyList())
        }
    }

    private fun onError(throwable: Throwable) {

        stateLiveData.value = ErrorSearchState(emptyList(), "${throwable.message}")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}
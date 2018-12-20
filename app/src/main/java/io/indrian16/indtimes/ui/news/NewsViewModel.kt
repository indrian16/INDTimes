package io.indrian16.indtimes.ui.news

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.repository.Repository
import io.indrian16.indtimes.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val newsState = MutableLiveData<NewsState>()

    private val compositeDisposable = CompositeDisposable()

    init {

        newsState.value = NewsLoadingState(emptyList())
    }

    fun updateNews(category: String) {

        getNewsList(category)
    }

    fun restoreNews() {

        newsState.value = NewsDefaultState(obtainCurrentData())
    }

    fun refreshNews(category: String) {

        newsState.value = NewsLoadingState(emptyList())
        getNewsList(category)
    }

    private fun obtainCurrentData() = newsState.value?.dataList ?: emptyList()

    private fun getNewsList(category: String) {

        compositeDisposable += repository.getTopHeadlines(category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onReceivedList, this::onError)
    }

    private fun onReceivedList(dataList: List<Article>) {

        d {"get data from repo ${dataList.size}"}
        val currentData = obtainCurrentData().toMutableList()
        currentData.addAll(dataList)

        newsState.value = NewsDefaultState(currentData)
    }

    private fun onError(throwable: Throwable) {

        d { "OnError: ${throwable.message}" }
        newsState.value = NewsErrorState("Error ${throwable.message}", obtainCurrentData())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}
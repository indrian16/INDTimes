package io.indrian16.indtimes.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.repository.Repository
import io.indrian16.indtimes.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val newsListStateLiveData = MutableLiveData<NewsListState>()

    private val compositeDisposable = CompositeDisposable()

    init {

        newsListStateLiveData.value = LoadingState(emptyList())
    }

    fun updateNews(category: String) {

        getNewsList(category)
    }

    fun refreshNews(category: String) {

        newsListStateLiveData.value = LoadingState(emptyList())
        getNewsList(category)
    }

    private fun getNewsList(category: String) {

        compositeDisposable += repository.getTopHeadlines(category)
                .toObservable()
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReceivedList, this::onError)
    }

    private fun onReceivedList(dataList: List<Article>) {

        if (dataList.isNotEmpty()) {

            d {"get data from repo ${dataList.size}"}
            val currentData = obtainCurrentData().toMutableList()
            currentData.addAll(dataList)

            newsListStateLiveData.value = DefaultState(currentData)

        } else {

            newsListStateLiveData.value = EmptyState(emptyList())
        }
    }

    private fun onError(throwable: Throwable) {

        newsListStateLiveData.value = ErrorState(throwable.message.toString(), obtainCurrentData())
    }

    private fun obtainCurrentData() = newsListStateLiveData.value?.dataList ?: emptyList()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}
package io.indrian16.indtimes.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.model.Bookmark
import io.indrian16.indtimes.data.repository.LocalRepository
import io.indrian16.indtimes.util.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val localRepository: LocalRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var currentArticle: Article? = null

    val stateLiveData = MutableLiveData<DetailState>()

    fun receivedArticle(article: Article?) {

        currentArticle = article!!
        stateLiveData.value = DefaultDetailState(currentArticle!!)
    }

    fun saveBookmark() {

        var currentBookmark: Bookmark? = null
        currentArticle?.let { currentBookmark = Bookmark(

            saveTime = it.saveTime,
            author = it.author,
            content = it.content,
            description = it.description,
            publishedAt = it.publishedAt,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        ) }

        compositeDisposable += Observable.fromCallable {

            localRepository.saveBookmark(currentBookmark!!)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, this::onError)
    }

    private fun onError(throwable: Throwable) {

        d { "${throwable.message}" }
        stateLiveData.value = ErrorDetailState(currentArticle!!, throwable.message.toString())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}
package io.indrian16.indtimes.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private lateinit var currentArticle: Article
    val detailStateLiveData = MutableLiveData<DetailState>()

    fun receivedArticle(article: Article?) {

        currentArticle = article!!
        detailStateLiveData.value = DefaultState(currentArticle)
    }

    fun saveBookmark() {

        var currentBookmark: Bookmark?
        currentArticle.let { currentBookmark = Bookmark(

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
            .subscribe({

                detailStateLiveData.value = ChangeIconState(currentArticle, true)
                d { "Save Bookmark" }

            }, this::onError)
    }

    fun deleteBookmark() {

        compositeDisposable += Observable.fromCallable {

            localRepository.deleteBookmark(currentArticle.url)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                detailStateLiveData.value = ChangeIconState(currentArticle, false)
                d { "Delete Bookmark" }

            }, this::onError)
    }

    fun checkBookmarkIsExist(url: String) {

        compositeDisposable += localRepository.getBookmarkIsExist(url)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onReceivedBookmark, this::onError)
    }

    private fun onReceivedBookmark(bookmark: List<Bookmark>) {

        if (bookmark.isNotEmpty()) {

            d { "Bookmark Exist" }
            detailStateLiveData.value = ChangeIconState(currentArticle, true)

        } else {

            detailStateLiveData.value = ChangeIconState(currentArticle, false)
            d { "Bookmark Not Exist" }
        }
    }

    private fun onError(throwable: Throwable) {

        d { "${throwable.message}" }
        detailStateLiveData.value = ErrorState(currentArticle, throwable.message.toString())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}
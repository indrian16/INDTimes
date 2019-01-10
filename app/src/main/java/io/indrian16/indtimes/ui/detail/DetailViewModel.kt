package io.indrian16.indtimes.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.model.Favorite
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

    fun addFavorite() {

        val currentFavorite = currentArticle.let { Favorite (

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

            localRepository.addFavorite(currentFavorite)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                detailStateLiveData.value = ChangeIconState(currentArticle, true)
                d { "Save Bookmark" }

            }, this::onError)
    }

    fun deleteFavorite() {

        compositeDisposable += Observable.fromCallable {

            localRepository.deleteFavorite(currentArticle.url)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                detailStateLiveData.value = ChangeIconState(currentArticle, false)
                d { "Delete Bookmark" }

            }, this::onError)
    }

    fun checkFavorite(url: String) {

        compositeDisposable += localRepository.getFavoriteIsExist(url)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onReceivedFavoriteList, this::onError)
    }

    private fun onReceivedFavoriteList(bookmark: List<Favorite>) {

        if (bookmark.isNotEmpty()) {

            d { "Favorite Exist" }
            detailStateLiveData.value = ChangeIconState(currentArticle, true)

        } else {

            detailStateLiveData.value = ChangeIconState(currentArticle, false)
            d { "Favorite Not Exist" }
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
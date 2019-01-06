package io.indrian16.indtimes.data.repository

import android.content.Context
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.data.api.ArticleModel
import io.indrian16.indtimes.data.api.NewsService
import io.indrian16.indtimes.data.db.ArticleDao
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.util.AppConstant
import io.indrian16.indtimes.util.isConnAvailable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(private val context: Context,
                                     private val localRepository: LocalRepository,
                                     private val newsService: NewsService) {

    fun getTopHeadlines(category: String): Single<List<Article>> {

        return if (context.isConnAvailable()) {

            newsService.getTopHeadlines(AppConstant.COUNTRY_ID, category, AppConstant.API_KEY)
                .toObservable()
                .flatMapIterable { it.articles }
                .map { articleModelToArticle(it) }
                .doOnNext {

                    localRepository.saveToLocal(it)
                }
                .toList()
        } else {

            localRepository.getTopHeadlineFromLocal()
        }
    }

    fun getEverything(query: String): Single<List<Article>> {

        return newsService.getEverything(query, AppConstant.COUNTRY_ID, AppConstant.API_KEY)
            .toObservable()
            .flatMapIterable { it.articles }
            .map { articleModelToArticle(it) }
            .toList()
    }

    private fun articleModelToArticle(model: ArticleModel): Article {

        return Article(
            saveTime = Date(System.currentTimeMillis()),
            author = model.author,
            content = model.content,
            description = model.description,
            publishedAt = model.publishedAt,
            title = model.title,
            url = model.url,
            urlToImage = model.urlToImage
        )
    }
}
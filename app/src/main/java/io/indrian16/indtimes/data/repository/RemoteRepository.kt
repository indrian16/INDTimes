package io.indrian16.indtimes.data.repository

import io.indrian16.indtimes.data.api.ArticleModel
import io.indrian16.indtimes.data.api.NewsService
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.util.AppConstant
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val newsService: NewsService) {

    fun getTopHeadlines(category: String): Observable<Article> {

        return newsService.getTopHeadlines(category = category, country = AppConstant.COUNTRY_ID, apiKey = AppConstant.API_KEY)
            .toObservable()
            .flatMapIterable { it.articles }
            .map { articleModelToArticle(it) }
    }

    fun getEverything(query: String): Single<List<Article>> {

        return newsService.getEverything(query = query, language = AppConstant.COUNTRY_ID, apiKey = AppConstant.API_KEY)
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
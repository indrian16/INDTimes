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
import javax.inject.Inject

class Repository @Inject constructor(private val context: Context,
                                     private val articleDao: ArticleDao,
                                     private val newsService: NewsService) {

    fun getTopHeadlines(category: String): Single<List<Article>> {

        return if (context.isConnAvailable()) {

            newsService.getTopHeadlines(AppConstant.COUNTRY_ID, category, AppConstant.API_KEY)
                .toObservable()
                .flatMapIterable { it.articles }
                .map { articleModelToArticle(it) }
                .doOnNext {

                    d { "Insert article --> ${it.title}" }
                    saveInDB(it)
                }
                .toList()
        } else {

            articleDao.getTopHeadlines()
        }
    }

    fun getEverything(query: String): Single<List<Article>> {

        return newsService.getEverything(query, AppConstant.COUNTRY_ID, AppConstant.API_KEY)
            .toObservable()
            .flatMapIterable { it.articles }
            .map { articleModelToArticle(it) }
            .doOnNext { saveInDB(it) }
            .toList()
    }

    private fun saveInDB(article: Article?) {

        if (article != null) articleDao.insertArticle(article)
        else d {"article null"}
    }

    private fun articleModelToArticle(model: ArticleModel): Article {

        return Article(
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
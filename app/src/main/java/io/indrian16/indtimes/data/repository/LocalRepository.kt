package io.indrian16.indtimes.data.repository

import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.data.db.ArticleDao
import io.indrian16.indtimes.data.db.FavoriteDao
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.model.Favorite
import javax.inject.Inject

class LocalRepository @Inject constructor(private val articleDao: ArticleDao,
                                          private val favoriteDao: FavoriteDao) {

    fun getTopHeadlineFromLocal() = articleDao.getTopHeadlines()

    fun saveToLocal(article: Article?) {

        if (article != null) {

            d { "Insert article --> ${article.title}" }
            articleDao.insertArticle(article)
        } else {

            d { "article is null" }
        }
    }

    fun getFavoriteList() = favoriteDao.getFavorites()

    fun addFavorite(favorite: Favorite) = favoriteDao.insertFavorite(favorite)

    fun deleteFavorite(url: String) = favoriteDao.deleteFavorite(url)

    fun getFavoriteIsExist(url: String) = favoriteDao.getFavoriteIsExist(url)
}
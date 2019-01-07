package io.indrian16.indtimes.data.repository

import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.data.db.ArticleDao
import io.indrian16.indtimes.data.db.BookmarkDao
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.model.Bookmark
import javax.inject.Inject

class LocalRepository @Inject constructor(private val articleDao: ArticleDao,
                                          private val bookmarkDao: BookmarkDao) {

    fun getTopHeadlineFromLocal() = articleDao.getTopHeadlines()

    fun saveToLocal(article: Article?) {

        if (article != null) {

            d { "Insert article --> ${article.title}" }
            articleDao.insertArticle(article)
        } else {

            d { "article is null" }
        }
    }

    fun saveBookmark(bookmark: Bookmark) = bookmarkDao.insertBookmark(bookmark)

    fun deleteBookmark(url: String) = bookmarkDao.deleteBookmark(url)

    fun getBookmarkIsExist(url: String) = bookmarkDao.getBookmarkIsExist(url)
}
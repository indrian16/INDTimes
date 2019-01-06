package io.indrian16.indtimes.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.indrian16.indtimes.data.model.Article
import io.reactivex.Single

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article)

    @Query("SELECT * FROM articles ORDER BY saveTime DESC")
    fun getTopHeadlines(): Single<List<Article>>
}
package io.indrian16.indtimes.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.indrian16.indtimes.data.model.Article
import io.reactivex.Single

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article)

    @Query("SELECT * FROM articles ORDER BY saveTime DESC")
    fun getTopHeadlines(): Single<List<Article>>
}
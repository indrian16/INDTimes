package io.indrian16.indtimes.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.indrian16.indtimes.data.model.Bookmark
import io.reactivex.Single

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM bookmarks")
    fun getBookmarks(): Single<List<Bookmark>>

    @Query("DELETE FROM bookmarks WHERE url = :url")
    fun deleteBookmark(url: String)

    @Query("SELECT * FROM bookmarks WHERE url = :url")
    fun getBookmarkIsExist(url: String): Single<List<Bookmark>>
}
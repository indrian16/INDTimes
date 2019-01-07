package io.indrian16.indtimes.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
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
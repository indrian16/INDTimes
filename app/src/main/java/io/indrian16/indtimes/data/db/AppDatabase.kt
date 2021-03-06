package io.indrian16.indtimes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.model.Favorite
import io.indrian16.indtimes.util.AppConstant

@Database(entities = [Article::class, Favorite::class], version = AppConstant.DB_VER, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    abstract fun favoriteDao(): FavoriteDao
}
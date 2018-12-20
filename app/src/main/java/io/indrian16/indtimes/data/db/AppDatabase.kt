package io.indrian16.indtimes.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.util.AppConstant

@Database(entities = [Article::class], version = AppConstant.DB_VER, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
}
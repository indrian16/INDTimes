package io.indrian16.indtimes.di.module

import android.app.Application
import androidx.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import io.indrian16.indtimes.data.db.AppDatabase
import io.indrian16.indtimes.util.AppConstant
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: Application) = application.applicationContext!!

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {

        return Room.databaseBuilder(context, AppDatabase::class.java, AppConstant.DB_NAME)
            .build()
    }

    @Provides
    fun provideArticleDao(appDatabase: AppDatabase) = appDatabase.articleDao()

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase) = appDatabase.favoriteDao()
}
package io.indrian16.indtimes.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import io.indrian16.indtimes.data.api.NewsService
import io.indrian16.indtimes.data.db.ArticleDao
import io.indrian16.indtimes.data.db.BookmarkDao
import io.indrian16.indtimes.data.repository.LocalRepository
import io.indrian16.indtimes.data.repository.Repository

@Module
class RepositoryModule {

    @Provides
    fun provideLocalRepository(articleDao: ArticleDao, bookmarkDao: BookmarkDao) = LocalRepository(articleDao, bookmarkDao)

    @Provides
    fun provideRepository(context: Context,
                          localRepository: LocalRepository,
                          newsService: NewsService): Repository {

        return Repository(context, localRepository, newsService)
    }
}
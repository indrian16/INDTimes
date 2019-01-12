package io.indrian16.indtimes.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import io.indrian16.indtimes.data.api.NewsService
import io.indrian16.indtimes.data.db.ArticleDao
import io.indrian16.indtimes.data.db.FavoriteDao
import io.indrian16.indtimes.data.repository.LocalRepository
import io.indrian16.indtimes.data.repository.RemoteRepository
import io.indrian16.indtimes.data.repository.Repository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    fun provideLocalRepository(articleDao: ArticleDao, favoriteDao: FavoriteDao) = LocalRepository(articleDao, favoriteDao)

    @Provides
    fun provideRemoteRepository(newsService: NewsService) = RemoteRepository(newsService)

    @Provides
    @Singleton
    fun provideRepository(context: Context,
                          localRepository: LocalRepository,
                          remoteRepository: RemoteRepository): Repository {

        return Repository(context, localRepository, remoteRepository)
    }
}
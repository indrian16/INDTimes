package io.indrian16.indtimes.ui.news

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import io.indrian16.indtimes.ViewModelFactory
import io.indrian16.indtimes.data.repository.Repository

@Module
class NewsFragmentModule {

    @Provides
    fun provideNewsViewModel(repository: Repository): NewsViewModel {

        return NewsViewModel(repository)
    }

    @Provides
    fun provideNewsViewModelFactory(newsViewModel: NewsViewModel): ViewModelProvider.Factory {

        return ViewModelFactory(newsViewModel)
    }
}
package io.indrian16.indtimes.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.indrian16.indtimes.ui.news.NewsFragment
import io.indrian16.indtimes.ui.news.NewsFragmentModule

@Module
abstract class MainActivityProvider {

    @ContributesAndroidInjector(modules = [NewsFragmentModule::class])
    abstract fun provideNewsFragment(): NewsFragment
}
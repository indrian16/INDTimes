package io.indrian16.indtimes.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.indrian16.indtimes.ui.favorite.FavoriteFragment
import io.indrian16.indtimes.ui.news.NewsFragment

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun contributeNewsFragment(): NewsFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment
}
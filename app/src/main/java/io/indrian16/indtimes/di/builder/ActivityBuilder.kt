package io.indrian16.indtimes.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.indrian16.indtimes.ui.main.MainActivity
import io.indrian16.indtimes.ui.search.SearchActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [FragmentBuilder::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeSearchActivity(): SearchActivity
}
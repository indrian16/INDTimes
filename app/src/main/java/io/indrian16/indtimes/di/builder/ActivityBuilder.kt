package io.indrian16.indtimes.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.indrian16.indtimes.ui.main.MainActivity
import io.indrian16.indtimes.ui.main.MainActivityProvider

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityProvider::class])
    abstract fun bindMainActivity(): MainActivity
}
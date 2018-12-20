package io.indrian16.indtimes.di.module

import dagger.Module
import dagger.Provides
import io.indrian16.indtimes.data.api.NewsService
import io.indrian16.indtimes.util.AppConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {

            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) =

            Retrofit.Builder()
                .baseUrl(AppConstant.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()!!

    @Provides
    fun provideNewsService(retrofit: Retrofit) = retrofit.create(NewsService::class.java)!!
}
package io.indrian16.indtimes.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    fun getTopHeadlines(@Query("country") country: String,
                        @Query("category") category: String,
                        @Query("apiKey") apiKey: String): Single<GetNewsResponse>
}
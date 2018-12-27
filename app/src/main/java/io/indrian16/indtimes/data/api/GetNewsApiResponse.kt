package io.indrian16.indtimes.data.api

data class GetNewsApiResponse(
    val articles: List<ArticleModel>,
    val status: String,
    val totalResults: Int
)
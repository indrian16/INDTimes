package io.indrian16.indtimes.data.api

data class GetNewsResponse(
    var articles: List<ArticleModel>,
    val status: String,
    val totalResults: Int
)
package io.indrian16.indtimes.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import io.indrian16.indtimes.util.AppConstant

@Entity(tableName = AppConstant.TABLE_ARTICLE,
        indices = [ Index(value = ["url"]) ],
        primaryKeys = ["url"])
data class Article(
    val author: String?,
    val content: String?,
    val description: String,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String
)
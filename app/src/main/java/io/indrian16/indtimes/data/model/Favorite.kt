package io.indrian16.indtimes.data.model

import androidx.room.Entity
import androidx.room.Index
import android.os.Parcelable
import io.indrian16.indtimes.util.AppConstant
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = AppConstant.TABLE_FAVORITE,
        primaryKeys = ["url"],
        indices = [(Index("url"))])
@Parcelize
data class Favorite(
    val saveTime: Date?,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String,
    val urlToImage: String?
): Parcelable
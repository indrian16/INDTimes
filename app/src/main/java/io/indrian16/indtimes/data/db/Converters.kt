package io.indrian16.indtimes.data.db

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let {

        Date(it)
    }

    @TypeConverter
    fun dataToTimestamp(date: Date?) = date?.time
}
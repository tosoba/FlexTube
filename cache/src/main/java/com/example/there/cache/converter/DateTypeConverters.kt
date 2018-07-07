package com.example.there.cache.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateTypeConverters {
    @TypeConverter
    fun toDate(value: Long): Date = Date(value)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time
}
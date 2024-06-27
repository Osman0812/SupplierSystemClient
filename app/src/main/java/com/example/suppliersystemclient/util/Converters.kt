package com.example.suppliersystemclient.util

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }

    @TypeConverter
    fun listToString(list: List<Int>?): String? {
        return list?.joinToString(",")
    }
}

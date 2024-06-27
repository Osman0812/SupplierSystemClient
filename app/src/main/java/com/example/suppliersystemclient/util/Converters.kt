package com.example.suppliersystemclient.util

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        return value?.takeIf { it.isNotEmpty() }?.split(",")?.mapNotNull {
            it.toIntOrNull()
        }
    }

    @TypeConverter
    fun fromArrayList(list: List<Int>?): String? {
        return list?.joinToString(",")
    }
}
package com.abdurraahm.spellcorrect.data.local.converter

import androidx.room.TypeConverter

class IntSetConverter {
    @TypeConverter
    fun fromString(value: String): Set<Int> {
        return if (value.isEmpty()) {
            emptySet()
        } else {
            value.split(",").map { it.toInt() }.toSet()
        }
    }

    @TypeConverter
    fun toString(set: Set<Int>): String {
        return set.joinToString(",")
    }
}
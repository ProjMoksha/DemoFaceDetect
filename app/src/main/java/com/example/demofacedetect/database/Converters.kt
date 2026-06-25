package com.example.demofacedetect.database

import androidx.room.TypeConverter

/** Converts embeddings to compact JSON-like text. Kept simple to avoid adding a JSON parser dependency. */
class Converters {
    @TypeConverter fun fromFloatArray(value: FloatArray): String = value.joinToString(prefix = "[", postfix = "]")
    @TypeConverter fun toFloatArray(value: String): FloatArray = value.trim('[',']').takeIf { it.isNotBlank() }?.split(',')?.map { it.trim().toFloatOrNull() ?: 0f }?.toFloatArray() ?: FloatArray(0)
}

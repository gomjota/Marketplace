package com.juangomez.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.juangomez.database.entities.DatabaseProductEntity


class ProductListConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToProductEntityList(data: String?): List<DatabaseProductEntity>? {
        if (data == null) {
            return mutableListOf()
        }

        val listType = object : TypeToken<List<DatabaseProductEntity>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun productEntityListToString(productEntities: List<DatabaseProductEntity>): String {
        return gson.toJson(productEntities)
    }
}
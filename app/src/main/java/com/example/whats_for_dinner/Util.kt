package com.example.whats_for_dinner

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

data class TempDish (
    var id: Int,
    var name: String,
    var type: String,
    var serverId: Int,
    var timestamp: Long,
)

inline fun <reified T> parseArray(json: String, typeToken: Type): T {
    val gson = GsonBuilder().create()
    return gson.fromJson<T>(json, typeToken)
}
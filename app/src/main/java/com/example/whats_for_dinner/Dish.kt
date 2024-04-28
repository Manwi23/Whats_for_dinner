package com.example.whats_for_dinner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: consider adding a separate type table

@Entity(tableName = "dish_list")
class Dish(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "server_id")
    var serverId: Int = -1,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = -1,
    @ColumnInfo(name = "note")
    var note: String = "",
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0;

    companion object {
        fun fieldDict(): MutableMap<String, Boolean> {
            return mutableMapOf(
                "id" to false,
                "name" to false,
                "type" to false,
                "serverId" to false,
                "timestamp" to false,
                "note" to false
            )
        }
    }

    fun prettyPrint(): String {
        return "$id, $type, $note"
    }
}
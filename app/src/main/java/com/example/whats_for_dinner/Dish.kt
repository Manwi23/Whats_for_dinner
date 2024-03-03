package com.example.whats_for_dinner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: consider adding a separate type table

@Entity(tableName = "dish_list")
class Dish(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "server_id")
    val server_id: Int = -1,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = -1,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0;
}
package com.example.whats_for_dinner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: consider adding a separate type table

@Entity(tableName = "dish_list")
class Dish (name : String, type : String, server_id : Int = -1) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0;
    @ColumnInfo(name = "name")
    val name: String = name;
    @ColumnInfo(name = "type")
    val type: String = type;
    @ColumnInfo(name = "server_id")
    var server_id: Int = server_id;
}

data class TempDish (
    var id: Int,
    var name: String,
    var type: String,
    var server_id: Int,
)

data class ListTempDish (
    var list: List<TempDish>,
)
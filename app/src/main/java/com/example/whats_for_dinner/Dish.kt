package com.example.whats_for_dinner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//data class Dish(val name: String, val type: String)

@Entity(tableName = "dish_list")
class Dish(
//    @PrimaryKey(autoGenerate = true) val id: Int,
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String
)
package com.example.whats_for_dinner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish_list")
class Dish (name : String, type : String){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0;
    @ColumnInfo(name = "name")
    val name: String = name;
    @ColumnInfo(name = "type")
    val type: String = type;
}
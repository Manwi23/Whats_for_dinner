package com.example.whats_for_dinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DishListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DishListAdapter.DishViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var dishes = emptyList<Dish>() // Cached copy of dishes
    private var types = emptyList<String>() // Cached copy of types

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return DishViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        val builder = StringBuilder()
        builder.append(dish.name)
            .append(", ")
            .append(dish.type)
        holder.dishItemView.text = builder.toString()
    }

    internal fun setDishes(dishes: List<Dish>) {
        this.dishes = dishes
        notifyDataSetChanged()
    }

    internal fun setTypes(types: List<String>) {
        this.types = types
        notifyDataSetChanged()
    }

    override fun getItemCount() = dishes.size
}
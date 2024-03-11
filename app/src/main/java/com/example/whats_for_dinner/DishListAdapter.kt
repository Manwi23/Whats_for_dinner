package com.example.whats_for_dinner

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.metadata.jvm.JvmProtoBuf.flags


class DishListAdapter (private var context: Context, private var dishViewModel: DishViewModel) : RecyclerView.Adapter<DishListAdapter.DishViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var dishes = emptyList<Dish>() // Cached copy of dishes
    private var types = emptyList<String>() // Cached copy of types


    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishItemView: TextView = itemView.findViewById(R.id.textView)
        val storedView: View = itemView
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
        if (dish.timestamp == (-1).toLong()) {
            holder.dishItemView.paintFlags =
                holder.dishItemView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.dishItemView.paintFlags = holder.dishItemView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.storedView.setOnLongClickListener {
            val popupMenu = PopupMenu(context, holder.storedView)
            popupMenu.setOnMenuItemClickListener{
                if (it.itemId == R.id.action_delete) {
                    GlobalScope.launch{
                        dishViewModel.markToDelete(dish.id)
                    }
                }
                true
            }
            popupMenu.inflate(R.menu.dish_menu)
            popupMenu.show()

            true
        }
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
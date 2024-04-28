package com.example.whats_for_dinner

import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private


class DishListAdapter (private var context: Context, private var dishViewModel: DishViewModel) : RecyclerView.Adapter<DishListAdapter.DishViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var dishes = emptyList<Dish>() // Cached copy of dishes
    private var types = emptyList<String>() // Cached copy of types
    private lateinit var onClickEditDish: (id: Int) -> Unit

    constructor(context: Context, dishViewModel: DishViewModel, onClickEditDishFun: (id: Int) -> Unit) : this(context, dishViewModel) {
        onClickEditDish = onClickEditDishFun
    }

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
        val tempDeleted = dish.timestamp == (-1).toLong()
        val stringBuilder = StringBuilder()
        stringBuilder.append(dish.name)
            .append(", ")
            .append(dish.type)
        holder.dishItemView.text = stringBuilder.toString()
        if (tempDeleted) {
            holder.dishItemView.paintFlags =
                holder.dishItemView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.dishItemView.paintFlags = holder.dishItemView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.storedView.setOnLongClickListener {
            val popupMenu = PopupMenu(context, holder.storedView)
            popupMenu.setOnMenuItemClickListener{
                if (it.itemId == R.id.action_delete) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder
                        .setTitle("Delete dish?")
                        .setMessage("This action cannot be reversed.")
                        .setPositiveButton("Yes, continue") { _, _ ->
                            GlobalScope.launch{
                                dishViewModel.markToDelete(dish.id)
                            }
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                }
                if (it.itemId == R.id.action_edit) {
                    GlobalScope.launch {
                        onClickEditDish(dish.id)
                    }
                }
                true
            }
            popupMenu.inflate(R.menu.dish_menu)
            if (tempDeleted) {
                popupMenu.menu.findItem(R.id.action_edit).setEnabled(false)
                popupMenu.menu.findItem(R.id.action_delete).setEnabled(false)
            }
            popupMenu.show()

            true
        }


        holder.storedView.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            val dishView = inflater.inflate(R.layout.view_dish, null)
            dishView.findViewById<TextView>(R.id.dish_name_view).text = dish.name
            dishView.findViewById<TextView>(R.id.dish_type_view).text = dish.type
            dishView.findViewById<TextView>(R.id.dish_note_view).text = dish.note
            builder.setView(dishView)
                .setPositiveButton("Done") { _, _ ->}
            val dialog: AlertDialog = builder.create()
            dialog.show()
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
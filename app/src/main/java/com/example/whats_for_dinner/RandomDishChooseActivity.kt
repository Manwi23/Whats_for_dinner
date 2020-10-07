package com.example.whats_for_dinner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class RandomDishChooseActivity : AppCompatActivity() {

    private lateinit var dishViewModel: DishViewModel
    private lateinit var textView: TextView
    private lateinit var dishDatabase: DishRoomDatabase

    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_dish_choose)

        textView = findViewById(R.id.textView)

        dishViewModel = ViewModelProvider(this).get(DishViewModel::class.java)
        val types = listOf("All")

        val spinner: Spinner = findViewById(R.id.spinner)
        val arrayList = ArrayList(types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        GlobalScope.launch{
            val rest = ArrayList(dishViewModel.getAllTypes())
//            types.plus(rest)
//            val adapter2 = ArrayAdapter(this@RandomDishChooseActivity, android.R.layout.simple_spinner_item, types)
//            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            adapter.clear()
            adapter.addAll(rest)
            adapter.notifyDataSetChanged()
        }


        val buttonBack = findViewById<FloatingActionButton>(R.id.back)
        buttonBack.setOnClickListener {
            val replyIntent = Intent()
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }

        val buttonRand = findViewById<FloatingActionButton>(R.id.rand)
        buttonRand.setOnClickListener {
            val type = "All"
            if (type == "All") {
                GlobalScope.launch {
                    val dishes = dishViewModel.getAllDishes()
                    Log.i("Tag 1", dishes.toString())
                    if (dishes != null && dishes.isNotEmpty()) {
                        val randomDish = dishes.shuffled().take(1)[0]
                        val builder = StringBuilder()
                        builder.append(randomDish.name)
                            .append(", ")
                            .append(randomDish.type)
                        textView.text = builder.toString()
                    } else {
                        textView.text = "Add some dishes first!"
                    }
                }
            }
        }


    }

}


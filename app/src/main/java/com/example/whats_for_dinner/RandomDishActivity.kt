package com.example.whats_for_dinner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlin.collections.ArrayList


class RandomDishActivity : BaseActivity() {

    private lateinit var dishViewModel: DishViewModel
    private lateinit var textView: TextView
    private val scope = MainScope()

    private suspend fun getDishTypes() : ArrayList<String> {
        return withContext(Dispatchers.Default) {
            ArrayList(dishViewModel.getAllTypes())
        }
    }

    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.setReverse(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_dish)

        textView = findViewById(R.id.textView)

        dishViewModel = ViewModelProvider(this)[DishViewModel::class.java]
        val types = listOf("All")

        val spinner: Spinner = findViewById(R.id.spinner)
        val arrayList = ArrayList(types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        scope.launch {
            val rest = getDishTypes()
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
            val type = spinner.selectedItem.toString()
            if (type == "All") {
                scope.launch {
                    val dishes = dishViewModel.getAllDishes()
                    Log.i("Random", dishes.toString())
                    if (!dishes.isNullOrEmpty()) {
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
            } else {
                scope.launch {
                    val dishes = dishViewModel.dishesOfAType(type)
                    Log.i("Random", dishes.toString())
                    if (dishes.isNotEmpty()) {
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


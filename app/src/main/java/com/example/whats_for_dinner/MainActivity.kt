package com.example.whats_for_dinner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class MainActivity : AppCompatActivity() {

    private val newDishActivityRequestCode = 1
    private val randomDishActivityRequestCode = 2
    private lateinit var dishViewModel: DishViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(findViewById(R.id.toolbar))

        // Get a new or existing ViewModel from the ViewModelProvider.
        dishViewModel = ViewModelProvider(this).get(DishViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = DishListAdapter(this, dishViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        dishViewModel.allDishes.observe(this, Observer { dishes ->
            // Update the cached copy of the words in the adapter.
            dishes?.let { adapter.setDishes(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddDishActivity::class.java)
            startActivityForResult(intent, newDishActivityRequestCode)
        }

        val random = findViewById<FloatingActionButton>(R.id.rand)
        random.setOnClickListener {
            val intent = Intent(this@MainActivity, RandomDishChooseActivity::class.java)
            startActivityForResult(intent, randomDishActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newDishActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
//                val dish_data_list = data.getStringArrayListExtra(AddDishActivity.EXTRA_REPLY)
                val dishDataArray = data.getStringArrayExtra(AddDishActivity.EXTRA_REPLY)
                val dish = dishDataArray?.get(0)?.let { Dish(it, dishDataArray[1]) }
                if (dish != null) {
                    dishViewModel.insert(dish)
                }
                Unit
            }
        } else if (requestCode == newDishActivityRequestCode && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(
                    applicationContext,
                    R.string.not_implemented,
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }



}
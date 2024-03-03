package com.example.whats_for_dinner

//import io.ktor.client.features.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//import kotlinx.android.synthetic.main.recyclerview_item.view.*

class MainActivity : AppCompatActivity() {

    private val newDishActivityRequestCode = 1
    private val randomDishActivityRequestCode = 2
    private lateinit var dishViewModel: DishViewModel
    private val client = HttpClient(CIO)


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

        // Add an observer on the LiveData returned by allDishes.
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newDishActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
//                val dish_data_list = data.getStringArrayListExtra(AddDishActivity.EXTRA_REPLY)
                val dishDataArray = data.getStringArrayExtra(AddDishActivity.EXTRA_REPLY)
                val dish = dishDataArray?.get(0)?.let { Dish(it, dishDataArray[1], -1, java.time.Instant.now().toEpochMilli()) }
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

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menu is MenuBuilder) {
            (menu as MenuBuilder).setOptionalIconsVisible(true)
        }
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private suspend fun syncToServer() {
        var text = "placeholder"
        try {
            val dishesList = dishViewModel.getAllDishes() ?: listOf()
            println(dishesList)
//            val dishes = JSONArray(dishesList)
            val gson = Gson()
            val dishes = gson.toJson(dishesList)
            println(dishes)
//            val response: HttpResponse = client.get("http://10.0.2.2:5000") // emulator
//            val response: HttpResponse = client.get("http://192.168.1.23:5000") // real device
            val response: HttpResponse = client.post("http://192.168.1.23:5000/") {
                contentType(ContentType.Application.Json)
                setBody(dishes)
                url {
                    path("sync")
                }
            }

            text = response.body()
            println(text)
            println("here")
            val type = object : TypeToken<List<TempDish>>() {}.type
            val dishList = parseArray<List<TempDish>>(text, type)
            for (dish in dishList) {
                dishViewModel.updateServerId(dish.id, Dish(dish.name, dish.type, dish.server_id, dish.timestamp))
//                dishViewModel.updateDish(dish.id, Dish(dish.name, dish.type, dish.server_id))
            }
        } catch (e: java.lang.Exception) {
            println(e.message)
            text = "ah nope"
        }
        this.runOnUiThread {
            Toast.makeText(
                applicationContext,
                text,
                Toast.LENGTH_LONG
            ).show()
        }
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
            R.id.action_sync -> {
                CoroutineScope(Dispatchers.IO).launch {
                    syncToServer()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }



}
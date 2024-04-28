package com.example.whats_for_dinner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.EXTRA_INDEX
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates


class EditDishActivity : BaseActivity() {

    private lateinit var editDishNameView: EditText
    private lateinit var noteView: EditText
    private val scope = MainScope()
    private lateinit var dishViewModel: DishViewModel
    private var plusClicked = false
    private lateinit var addNewTypeView : EditText

    private suspend fun getDishTypes() : ArrayList<String> {
        return withContext(Dispatchers.Default) {
            ArrayList(dishViewModel.getAllTypes())
        }
    }

    private suspend fun getDishById(id: Int) : Dish? {
        return withContext(Dispatchers.Default) {
            dishViewModel.getById(id)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dish)
        setupUI(findViewById(R.id.add_dish_main_layout))

        editDishNameView = findViewById(R.id.edit_dish_name)
        noteView = findViewById(R.id.note)
        dishViewModel = ViewModelProvider(this)[DishViewModel::class.java]
        plusClicked = false
        addNewTypeView = findViewById(R.id.new_type_name)
        val spinner: Spinner = findViewById(R.id.spinner_add_type)

        val arrayList = ArrayList<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)

        var currentTypes = ArrayList<String>()

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val editedDishId = intent.getIntExtra(EXTRA_INDEX, -1)

        scope.launch {
            val types = getDishTypes()
            adapter.addAll(types)
            currentTypes = types

            val editedDish = getDishById(editedDishId)
            if (editedDish == null) {
                val replyIntent = Intent()
                replyIntent.putExtra("EXTRA_REASON", "Internal error")
                setResult(Activity.RESULT_CANCELED, replyIntent)
                finish()
                return@launch
            }

            editDishNameView.setText(editedDish.name)
            spinner.setSelection(types.indexOf(editedDish.type))
            noteView.setText(editedDish.note)

            adapter.notifyDataSetChanged()
        }

        val button = findViewById<FloatingActionButton>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editDishNameView.text)) {
                Toast.makeText(
                    applicationContext,
                    R.string.provide_a_name_toast,
                    Toast.LENGTH_LONG
                ).show()
            } else if (currentTypes.size == 0) {
                Toast.makeText(
                    applicationContext,
                    R.string.select_type_first,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val dishName = editDishNameView.text.toString()
                val dishType = spinner.selectedItem.toString()
                val dishNote = noteView.text.toString()
                val dishDataArray = arrayOf(editedDishId.toString(), dishName, dishType, dishNote)
                replyIntent.putExtra(EXTRA_REPLY, dishDataArray)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
            hideKeyboard()
        }

        val buttonAddType = findViewById<FloatingActionButton>(R.id.add_type)
        buttonAddType.setOnClickListener {
            if (!plusClicked) {
                plusClicked = true
                val addTypePart = findViewById<LinearLayout>(R.id.add_new_type_layout)
                addTypePart.visibility = View.VISIBLE
            }
        }

        val buttonSaveType = findViewById<FloatingActionButton>(R.id.add_type_save)
        buttonSaveType.setOnClickListener {
            val newDishType = addNewTypeView.text.toString()
            if (!TextUtils.isEmpty(newDishType) && !currentTypes.contains(newDishType)) {
                adapter.add(newDishType)
                adapter.notifyDataSetChanged()
                currentTypes.add(newDishType)
                spinner.setSelection(currentTypes.size - 1)
                Toast.makeText(
                    applicationContext,
                    R.string.type_added,
                    Toast.LENGTH_SHORT
                ).show()
            } else if (currentTypes.contains(newDishType)) {
                Toast.makeText(
                    applicationContext,
                    R.string.type_present,
                    Toast.LENGTH_SHORT
                ).show()
                spinner.setSelection(currentTypes.indexOf(newDishType))
            }
            hideKeyboard()
        }

        val buttonBack = findViewById<FloatingActionButton>(R.id.back_button)
        buttonBack.setOnClickListener {
            hideKeyboard()
            val replyIntent = Intent()
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "Edit_new_dish_reply"
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        // Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, _ ->
                hideKeyboard()
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }
}


package com.example.whats_for_dinner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.example.whats_for_dinner.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AddDishActivity : AppCompatActivity() {

    private lateinit var editDishNameView: EditText
    private lateinit var editDishTypeView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dish)
        editDishNameView = findViewById(R.id.edit_dish_name)
        editDishTypeView = findViewById(R.id.edit_dish_type)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editDishNameView.text) || TextUtils.isEmpty(editDishTypeView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val dishName = editDishNameView.text.toString()
                val dishType = editDishTypeView.text.toString()
                val dishDataArray = arrayOf(dishName, dishType)
                replyIntent.putExtra(EXTRA_REPLY, dishDataArray)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        val buttonBack = findViewById<FloatingActionButton>(R.id.back_add_page)
        buttonBack.setOnClickListener {
            val replyIntent = Intent()
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "Add_new_word_reply"
    }
}


package com.example.whats_for_dinner

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    private var reverse = false
    override fun finish() {
        Log.d("finish", "called finish")
        super.finish()
        overridePendingTransitionExit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransitionExit()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransitionEnter()
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
         overridePendingTransitionEnter()
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    private fun overridePendingTransitionEnter() {
        Log.d("finish", "enter, reversed: $reverse")
        if (!reverse) {
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        } else {
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        }
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    private fun overridePendingTransitionExit() {
        Log.d("finish", "exit, reversed: $reverse")
        if (!reverse) {
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        } else {
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }

    fun setReverse(newReverse: Boolean) {
        reverse = newReverse
    }
}

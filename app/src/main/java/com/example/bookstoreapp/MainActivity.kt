package com.example.bookstoreapp

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.example.bookstoreapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.invalidUsernameTextView.isVisible = false
        binding.invalidPasswordTextView.isVisible = false
        binding.signInButton.setOnClickListener { launchList() }
    }

    private fun launchList() {
        val userId = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if(userId == "SS" && password == "11111") {
            listIntent = Intent(this, ListActivity::class.java)
            startActivity(listIntent)
            binding.usernameEditText.setText("")
            binding.passwordEditText.setText("")
            binding.invalidUsernameTextView.isVisible = false
            binding.invalidPasswordTextView.isVisible = false
        } else {
            if(userId == "" || userId != "SS") {
                binding.invalidUsernameTextView.isVisible = true
            }
            if(password == "" || password != "11111") {
                binding.invalidPasswordTextView.isVisible = true
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
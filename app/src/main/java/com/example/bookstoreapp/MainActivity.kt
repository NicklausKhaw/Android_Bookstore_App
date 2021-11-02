package com.example.bookstoreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.bookstoreapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        } else {
            Toast.makeText(applicationContext,"Invalid User ID or Password",Toast.LENGTH_SHORT).show()
        }
    }
}
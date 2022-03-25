package com.example.socialframe.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialframe.R
import com.example.socialframe.databinding.ActivityLogin2Binding
import com.example.socialframe.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}
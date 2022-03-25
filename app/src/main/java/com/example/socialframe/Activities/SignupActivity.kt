package com.example.socialframe.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialframe.R
import com.example.socialframe.databinding.ActivityMainBinding
import com.example.socialframe.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}
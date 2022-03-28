package com.example.socialframe.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialframe.R
import com.example.socialframe.databinding.ActivityMainBinding
import com.example.socialframe.databinding.ActivitySelectorBinding

class SelectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivitySelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.loginbtn.setOnClickListener(){
            startActivity(Intent(this,LoginActivity::class.java))
            overridePendingTransition(R.anim.slidein, R.anim.slideout)
        }
        binding.signbtn.setOnClickListener(){
            startActivity(Intent(this,SignupActivity::class.java))
            overridePendingTransition(R.anim.slidein, R.anim.slideout)
        }
    }
    override fun onBackPressed() {
    }
}
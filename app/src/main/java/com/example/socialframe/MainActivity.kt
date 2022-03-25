package com.example.socialframe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialframe.databinding.ActivityMainBinding
import java.nio.channels.Selector

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        startActivity(Intent(this,Selector::class.java))
        overridePendingTransition(R.anim.slidein, R.anim.slideout)
    }
}
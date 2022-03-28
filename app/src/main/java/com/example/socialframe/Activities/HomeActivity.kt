package com.example.socialframe.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.socialframe.Fragments.HomeFragment
import com.example.socialframe.Fragments.Profile
import com.example.socialframe.R
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.databinding.ActivityHomeBinding
import com.example.socialframe.databinding.ActivityLogin2Binding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
lateinit var OpenModel:MainViewModel
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var toolbar:Toolbar?=findViewById(R.id.mytoolbar)
        toolbar?.setTitle("")
        setSupportActionBar(toolbar)
        OpenModel = ViewModelProvider(this).get(MainViewModel::class.java)
        OpenModel.mycontext=this
        var searchbtn=findViewById<ImageButton>(R.id.searchbtn)
        var messagebtn=findViewById<ImageButton>(R.id.messagebtn)
        var profilebtn=findViewById<ImageButton>(R.id.profilebtn)
        searchbtn.setOnClickListener(){
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.fragmentContainerView.id,HomeFragment())
            transaction.commit()
        }
        messagebtn.setOnClickListener(){
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.fragmentContainerView.id,HomeFragment())
            transaction.commit()
        }
        profilebtn.setOnClickListener(){
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.fragmentContainerView.id,Profile())
            transaction.commit()
        }
        //Updating UI
        GlobalScope.launch {
            async {
                while(true) {
                    OpenModel.UpdateUI()
                    delay(1000)
                }
            }
        }
    }
    override fun onBackPressed(){
    }
}
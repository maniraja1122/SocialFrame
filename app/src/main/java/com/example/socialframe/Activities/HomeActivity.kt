package com.example.socialframe.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.Fragments.*
import com.example.socialframe.R
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.databinding.ActivityHomeBinding
import com.example.socialframe.databinding.ActivityLogin2Binding
import kotlinx.coroutines.*

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
            transaction.replace(binding.fragmentContainerView.id,Search())
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
        binding.bottomnav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.homebtn -> {
                    var transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.fragmentContainerView.id,HomeFragment())
                    transaction.commit()
                }
                R.id.settingbtn -> {
                    var transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.fragmentContainerView.id,Settings())
                    transaction.commit()
                }
                R.id.notificationbtn -> {

                }
            }
            true
        }
        OpenModel.VisitedUser.observe(this, Observer {
            if(it.Name!="NA") {
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(binding.fragmentContainerView.id, VisitedProfile())
                transaction.commit()
            }
        })
        //Change Comment Post
        OpenModel.OpenedCommentPost.observe(this, Observer {
            if(it!="") {
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(binding.fragmentContainerView.id, CommentFragment())
                transaction.commit()
            }
        })
        //Updating UI
        GlobalScope.launch(Dispatchers.IO) {
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
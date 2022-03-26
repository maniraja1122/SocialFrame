package com.example.socialframe.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.classes.User
import com.example.socialframe.databinding.ActivityLogin2Binding
import com.example.socialframe.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.backbtn.setOnClickListener(){
            startActivity(Intent(this,SelectorActivity::class.java))
            overridePendingTransition(R.anim.slidein, R.anim.slideout)
        }
        binding.btnreg.setOnClickListener(){
            var myemail=binding.myemail.text.toString()
            var mypassword=binding.mypassword.text.toString()
            if(!isValidEmailId(myemail)){
                binding.myemail.setError("Please Enter A Valid Email")
            }
            else if(!isValidPassword(mypassword)){
                Toast.makeText(applicationContext, "Please Enter A Valid Password (Must have a digit,capital and special characters)", Toast.LENGTH_SHORT).show()
            }
            else{
                var newuser= User("",myemail,mypassword)
                var auth= AuthHelper.manager.auth
                auth.signInWithEmailAndPassword(newuser.Email,newuser.Password).addOnCompleteListener(
                    OnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,HomeActivity::class.java))
                            overridePendingTransition(R.anim.slidein, R.anim.slideout)
                        }
                    }).addOnFailureListener(){
                    Toast.makeText(applicationContext, it.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                }
        }
        }
    }
    open fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
    //Password Checker
    open fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }
}

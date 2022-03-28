package com.example.socialframe.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.classes.User
import com.example.socialframe.databinding.ActivityMainBinding
import com.example.socialframe.databinding.ActivitySignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.backbtnlog.setOnClickListener(){
            startActivity(Intent(this,SelectorActivity::class.java))
            overridePendingTransition(R.anim.slidein, R.anim.slideout)
        }
        binding.btnlogin.setOnClickListener(){
            var myname=binding.myname.text.toString()
            var myemail=binding.myemail1.text.toString()
            var mypassword=binding.mypassword.text.toString()
            if(myname==""){
                binding.myname.setError("Please Enter A Name First !!!")
            }
            else if(!isValidEmailId(myemail)){
                binding.myemail1.setError("Please Enter A Valid Email")
            }
            else if(!isValidPassword(mypassword)){
                Toast.makeText(applicationContext, "Please Enter A Valid Password (Must have a digit,capital and special characters)", Toast.LENGTH_SHORT).show()
            }
            else {
                var newuser = User(myname, myemail, mypassword)
                var auth = AuthHelper.manager.auth
                auth.createUserWithEmailAndPassword(newuser.Email, newuser.Password)
                    .addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "User Registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                                newuser.key = AuthHelper.manager.auth.uid.toString()
                                AuthHelper.AddUser(newuser)
                                //startActivity(Intent(this, HomeActivity::class.java))
                                //overridePendingTransition(R.anim.slidein, R.anim.slideout)
                            }
                        }).addOnFailureListener() {
                    Toast.makeText(
                        applicationContext,
                        it.localizedMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    override fun onBackPressed() {
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
package com.example.socialframe.AuthFunctions

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.socialframe.Repository.FirebaseManager
import com.example.socialframe.classes.User
import com.google.android.gms.tasks.OnCompleteListener

object AuthHelper {
    var manager = FirebaseManager()
    fun AddUser(user:User){
        manager.db.getReference().child("Users").child(user.key).setValue(user)
    }
}
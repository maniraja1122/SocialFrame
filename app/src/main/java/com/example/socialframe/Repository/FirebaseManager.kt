package com.example.socialframe.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseManager {
    var db : FirebaseDatabase = FirebaseDatabase.getInstance()
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
}
package com.example.socialframe.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.FirebaseStorageKtxRegistrar

class FirebaseManager {
    var db : FirebaseDatabase = FirebaseDatabase.getInstance()
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    var storage = FirebaseStorage.getInstance()
    init{
        db.setPersistenceEnabled(true)
    }
}
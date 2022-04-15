package com.example.socialframe.ViewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.classes.Chat
import com.example.socialframe.classes.MessageModel
import com.example.socialframe.classes.Post
import com.example.socialframe.classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds


class MainViewModel:ViewModel() {
    var CurrentUser:MutableLiveData<User> = MutableLiveData()
    var AllPosts:MutableLiveData<List<Post>> = MutableLiveData()
    var AllUsers:MutableLiveData<List<User>> = MutableLiveData()
    var VisitedUser:MutableLiveData<User> = MutableLiveData()
    var OpenedCommentPost:MutableLiveData<String> =MutableLiveData()
    var MessageReciever:MutableLiveData<User> = MutableLiveData() // Reciever
    var AllMessages:MutableLiveData<HashMap<String,MutableLiveData<List<MessageModel>>>> = MutableLiveData()
    var AllChats:MutableLiveData<MutableList<Chat>> = MutableLiveData()
    var mycontext:Context?=null
    //Functions
    fun UpdateUI(){
        CoroutineScope(Dispatchers.IO).launch {
            async {
                UpdateUser()
            }
            async {
                UpdateAllPosts()
            }
            async {
                UpdateAllUsers()
            }
            async {
                UpdateMessages()
            }
            async {
                UpdateChats()
            }
        }
    }
    fun SetUI(){
        CoroutineScope(Dispatchers.IO).launch {
            async {
                SetUser()
            }
            async {
                SetAllUsers()
            }
            async {
                SetMessages()
            }
            async {
                SetChats()
            }
        }
    }
    fun SetAllUsers(){
        //Single Time
        CoroutineScope(Dispatchers.IO).launch {
            async {
        AuthHelper.manager.db.getReference().child("Users").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                AllUsers.value=(snapshot.children.mapNotNull {
                    it.getValue(User::class.java)
                }.toList()).reversed()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
            }
        }
    }
    fun UpdateAllUsers(){
        //Every Time
        CoroutineScope(Dispatchers.IO).launch {
            async {
                AuthHelper.manager.db.getReference().child("Users")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var arr = snapshot.children.mapNotNull {
                                it.getValue(User::class.java)
                            }.toList().reversed()
                            if (arr.size != AllUsers.value!!.size) {
                                AllUsers.value = arr
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }}
    }
    fun SetAllPosts(){
        //Single Time
        CoroutineScope(Dispatchers.IO).launch {
            async {
                AuthHelper.manager.db.getReference().child("Posts")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            AllPosts.value = (snapshot.children.mapNotNull {
                                it.getValue(Post::class.java)
                            }.toList()).reversed().filter {
                                CurrentUser.value!!.Followed.contains(it.author)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }}
    }
    fun UpdateAllPosts(){
        //Every Time
        CoroutineScope(Dispatchers.IO).launch {
            async {
                AuthHelper.manager.db.getReference().child("Posts")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var arr = (snapshot.children.mapNotNull {
                                it.getValue(Post::class.java)
                            }.toList()).reversed().filter {
                                CurrentUser.value!!.Followed.contains(it.author)
                            }
                            if (arr.size != AllPosts.value!!.size) {
                                AllPosts.value = arr
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }}
    }
    fun UpdateUser(){
        //Every Time
        CoroutineScope(Dispatchers.IO).launch {
            async {
                AuthHelper.manager.db.getReference().child("Users")
                    .child(AuthHelper.manager.auth.uid.toString())
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var updated = snapshot.getValue(User::class.java)
                            if (updated != null)
                                CurrentUser.value = updated
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }}
    }
    fun SetUser(){
        //Single Time
        CoroutineScope(Dispatchers.IO).launch{
            async{
                AuthHelper.manager.db.getReference().child("Users").child(AuthHelper.manager.auth.uid.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        CurrentUser.value=snapshot.getValue(User::class.java)
                        SetAllPosts()
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }
    fun UpdateMessages(){
        //Every Time
        CoroutineScope(Dispatchers.IO).launch{
            async{
                AuthHelper.manager.db.getReference().child("Messages").child(AuthHelper.manager.auth.uid.toString()).addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(childsnaps in snapshot.children){
                            var key = childsnaps.key
                            var messagelist=childsnaps.children.mapNotNull {
                                it.getValue(MessageModel::class.java)
                            }.toList()
                            if(AllMessages.value!!.containsKey(key!!)) {
                                AllMessages.value!![key!!]!!.value = messagelist
                            }
                            else{
                                AllMessages.value!![key!!]= MutableLiveData()
                                AllMessages.value!![key!!]!!.value=messagelist
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }
    fun SetMessages(){
        //Single Time
        CoroutineScope(Dispatchers.IO).launch{
            async{
                AuthHelper.manager.db.getReference().child("Messages").child(AuthHelper.manager.auth.uid.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(childsnaps in snapshot.children){
                            var key = childsnaps.key
                            var messagelist=childsnaps.children.mapNotNull {
                                it.getValue(MessageModel::class.java)
                            }.toList()
                            AllMessages.value!![key!!]= MutableLiveData()
                            AllMessages.value!![key!!]!!.value=messagelist
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }
    //Setting and Updating For Chats
    fun SetChats(){
        //Update Time
        CoroutineScope(Dispatchers.IO).launch{
            async{
                AuthHelper.manager.db.getReference().child("Messages").child(AuthHelper.manager.auth.uid.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(msgsnapshot: DataSnapshot) {
                        var myallchats= mutableListOf<Chat>()
                        var index=0
                        for(childsnaps in msgsnapshot.children){
                            var key = childsnaps.key
                            var messagelist=childsnaps.children.mapNotNull {
                                it.getValue(MessageModel::class.java)
                            }.toList()
                            AuthHelper.manager.db.getReference().child("Users").child(key!!).addListenerForSingleValueEvent(object:ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var myuser=snapshot.getValue(User::class.java)
                                    var mymessage=messagelist.last()
                                    var newchat=Chat(myuser!!,mymessage)
                                    myallchats.add(newchat)
                                    index++
                                    if(index==(msgsnapshot.childrenCount).toInt()) {
                                        var change = false
                                        for (i in 0..AllChats.value!!.size - 1) {
                                            if (myallchats[i].NotEqual(AllChats.value?.get(i)!!)) {
                                                change = true
                                            }
                                        }
                                        if (change || myallchats.size != AllChats.value!!.size) {
                                            myallchats.sortByDescending {
                                                it.MyMessage.messageTime
                                            }
                                            AllChats.value = myallchats
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }
    fun UpdateChats(){
        //Update Time
        CoroutineScope(Dispatchers.IO).launch{
            async{
                AuthHelper.manager.db.getReference().child("Messages").child(AuthHelper.manager.auth.uid.toString()).addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(msgsnapshot: DataSnapshot) {
                        var myallchats= mutableListOf<Chat>()
                        var index=0
                        for(childsnaps in msgsnapshot.children){
                            var key = childsnaps.key
                            var messagelist=childsnaps.children.mapNotNull {
                                it.getValue(MessageModel::class.java)
                            }.toList()
                            AuthHelper.manager.db.getReference().child("Users").child(key!!).addListenerForSingleValueEvent(object:ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var myuser=snapshot.getValue(User::class.java)
                                    var mymessage=messagelist.last()
                                    var newchat=Chat(myuser!!,mymessage)
                                    myallchats.add(newchat)
                                    index++
                                    if(index==(msgsnapshot.childrenCount).toInt()) {
                                        var change = false
                                        myallchats.sortByDescending {
                                            it.MyMessage.messageTime
                                        }
                                        for (i in 0..AllChats.value!!.size - 1) {
                                            if (myallchats[i].NotEqual(AllChats.value?.get(i)!!)) {
                                                change = true
                                            }
                                        }
                                        if (change || myallchats.size != AllChats.value!!.size) {
                                            AllChats.value = myallchats
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }
    fun ChangePic(s: Uri?){
        CoroutineScope(Dispatchers.IO).launch{
            async{
                var myref = AuthHelper.manager.storage.reference.child("ProfilePhotos").child(CurrentUser.value!!.key)
                myref.putFile(s!!)
                    .addOnSuccessListener {
                        myref.downloadUrl.addOnSuccessListener {
                            CurrentUser.value!!.MyPICUrl=it.toString()
                            AuthHelper.AddUser(CurrentUser.value!!)
                        }
                    }
            }
        }
    }
    init{
        AllPosts.value= mutableListOf()
        AllUsers.value= mutableListOf()
        AllChats.value= mutableListOf()
        AllMessages.value= HashMap()
        CurrentUser.value=User()
        VisitedUser.value=User()
        OpenedCommentPost.value=""
        MessageReciever.value=User()
        CoroutineScope(Dispatchers.IO).launch{
            async{
                SetUI()
            }
        }
    }
}
